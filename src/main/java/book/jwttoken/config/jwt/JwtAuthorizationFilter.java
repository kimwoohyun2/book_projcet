package book.jwttoken.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import book.jwttoken.auth.AuthUserDetails;
import book.user.model.User;
import book.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

// �ΰ� (����ڰ� �ش� �ڿ� ���ٿ� ���� ������ �ִ��� Ȯ���ϴ� ����)
//   -> BasicAuthenticationFilter�� ��ӹ޾� �����Ѵ�.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

    private UserRepository userRepository;

    // securityConfig������ ����� ���� �ʿ��� �����ڸ� �˸°� ����.
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    // �ΰ��� ���� ��ü���� ����Ͻ� ������ ����ִ�.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>  JwtAuthorizationFilter.java  39:"+request);
    	
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        
        // 1. �߱��ߴ� ��ū�� ����� �ִ��� Ȯ���Ѵ�.
        //     -> ���� ��ū�� ���ٸ� ������ ó������ �ش� ���͸� �ѱ��.
        //         -> �� ��� �ش� ��û�� �ƹ��� ������ ���� ���ϰ� �ȴ�.
        //              -> ���� �ش� ��û�� Ư�� ������ �䱸�ȴٸ� spring security�� �ٸ� ���� ���͵��� ����
        //                 �ᱹ ���ܸ� ���� AuthenticationEntryPoint���� �ش� ���ܸ� ó���ϰ� �ȴ�.
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        // 2. ��ū�� �����Ѵٸ� �ش� ��ū�� ��ȿ�� ��ū���� ���θ� Ȯ���ϴ� ������ ��ģ��. (���� ���� �����ϴ� ������ ��ģ��)
        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("username").asString();

        // 3. ���� �ش� ��ū�� Ȯ��(����)�ϴµ� �����Ѵٸ� JWTVerificationException�� ������ �ǰ�
        //    �ش� ��û ���� ��ū�� �������� �ʴ� ���� �Ȱ��� ó���ȴ�.
        if(username != null) {
            User user = userRepository.findByUsername(username);

            // 4. ��ū�� ��ȿ�� ���, ������ ��ť��Ƽ�� �������ִ� ���� ó���� ����,
            //    ��ū�� �̿��Ͽ� ������ü(authentication)�� �����
            //    �Ʒ� 5������ ��ť��Ƽ ���ǿ� ������ü�� �����Ѵ�.
            AuthUserDetails authUserDetails = new AuthUserDetails(user);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                    		authUserDetails, //���߿� ��Ʈ�ѷ����� DI�ؼ� �� �� ����ϱ� ����.
                            null, // �н������ �𸣴ϱ� null ó��, ������ ���� �����ϴ°� �ƴϴϱ�!!
                            authUserDetails.getAuthorities());

            // 5. ��ť��Ƽ ���ǿ� ������ü�� �����Ѵ�.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 6. ���ǿ� ���������� �����Ѵٸ� Stateless�ϴٴ� JWT�� ������ ������°� �ƴѰ� ���� �� �ְ�����
            //    ���ǿ� ����Ǵ� ���������� �ش� ��û�� ���� ���� Ȯ�ο����θ� ���ǰ�
            //	  �ش� ��û�� �Ϸ�Ǹ� �� �̻� ������ �ʰ� ������� ������ �����ϴ� ������ �߻����� �ʴ´�.
            
            // 7. �ռ��� �������� �������� ����Ѵٸ� �ش� ��û�� ���������� Controller���� ���޵ǰ� �ȴ�.
        }

        chain.doFilter(request, response);
    }

}