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

// 인가 (사용자가 해당 자원 접근에 대해 권한이 있는지 확인하는 절차)
//   -> BasicAuthenticationFilter를 상속받아 구현한다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

    private UserRepository userRepository;

    // securityConfig에서의 사용을 위해 필요한 생성자를 알맞게 만듦.
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    // 인가에 대한 전체적인 비즈니스 로직이 담겨있다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>  JwtAuthorizationFilter.java  39:"+request);
    	
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        
        // 1. 발급했던 토큰이 헤더에 있는지 확인한다.
        //     -> 만약 토큰이 없다면 별도의 처리없이 해당 필터를 넘긴다.
        //         -> 이 경우 해당 요청은 아무런 권한을 얻지 못하게 된다.
        //              -> 만약 해당 요청에 특정 권한이 요구된다면 spring security의 다른 여러 필터들을 거쳐
        //                 결국 예외를 던져 AuthenticationEntryPoint에서 해당 예외를 처리하게 된다.
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        // 2. 토큰이 존재한다면 해당 토큰이 유효한 토큰인지 여부를 확인하는 절차를 거친다. (쉽게 말해 인증하는 절차를 거친다)
        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("username").asString();

        // 3. 만약 해당 토큰을 확인(인증)하는데 실패한다면 JWTVerificationException을 던지게 되고
        //    해당 요청 역시 토큰이 존재하지 않는 경우와 똑같이 처리된다.
        if(username != null) {
            User user = userRepository.findByUsername(username);

            // 4. 토큰이 유효한 경우, 스프링 시큐리티가 수행해주는 권한 처리를 위해,
            //    토큰을 이용하여 인증객체(authentication)를 만들고
            //    아래 5번에서 시큐리티 세션에 인증객체를 저장한다.
            AuthUserDetails authUserDetails = new AuthUserDetails(user);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                    		authUserDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                            null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                            authUserDetails.getAuthorities());

            // 5. 시큐리티 세션에 인증객체를 저장한다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 6. 세션에 인증정보를 저장한다면 Stateless하다는 JWT의 장점이 사라지는거 아닌가 물을 수 있겠지만
            //    세션에 저장되는 인증정보는 해당 요청에 대한 권한 확인용으로만 사용되고
            //	  해당 요청이 완료되면 더 이상 사용되지 않고 사라지기 때문에 걱정하는 문제는 발생하지 않는다.
            
            // 7. 앞서의 검증들을 문제없이 통과한다면 해당 요청은 정상적으로 Controller까지 전달되게 된다.
        }

        chain.doFilter(request, response);
    }

}