package book.jwttoken.config.jwt;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import book.user.dto.UserDto;
import book.jwttoken.auth.AuthUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

// ���� (�ſ� Ȯ�� ����)
//   -> JwtAuthenticationFilter�� �����ܰ踦 ó���ϴ� ���ͷν�
//		spring security���� ������ ������ �ϴ� UsernamePasswordAuthenticationFilter�� ��ӹ޾� �����Ѵ�.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;

    // Authentication ��ü�� ���� �����Ѵ�.
    // client�� POST: /login ��û�� �ϸ� attemptAuthentication �޼ҵ尡 ����ȴ�.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
    	
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>  JwtAuthenticationFilter.java  42:"+request);

        // 1. ObjectMapper�� �̿��� request�� ����ִ� ����� ������ DTO��ü(userDto)�� �����.
        ObjectMapper om = new ObjectMapper();
        UserDto userDto = null;
        try {
        	userDto = om.readValue(request.getInputStream(), UserDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. userDto������ spring security���� �̿��� �� �ְ� tokenȭ ��Ű��
        //    �ش� token���� authenticationManager.authenticate�� �̿��Ͽ� ������ �����ϰ� �ȴ�.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                		userDto.getUsername(),
                		userDto.getPassword());

        // 3. �Ʒ��� authenticate() �Լ��� ȣ�� �Ǹ� 
        //       -> (1) UserDetailSService(������ ��ť��Ƽ�� ����� interface)�� �������� AuthUserDetails�� �ִ�
        //				loadUserByUsername�� ���� ���ι��̴��� ȣ���Ͽ� DB���� ����ڸ� ��ȸ�ؿ´�.
        //			(2)	���� ����ڰ� ���� ��� UserNotFoundException ���ܸ� �߻���Ű��
        //				�����ϸ� UserDetails ��ü�� ��ȯ�Ѵ�.
        //			(3) ��ȯ ���� UserDetails ��ü�� ����� Password(DB��)��
        //				�α��ν� �Է��� Password��
        //				(Authentication�� ����� Password, ��ū�� �ι�° �Ķ����(credential))
        //				matches �޼���� ��. ��ġ���� �ʴ´ٸ� BadCredentialException ���ܸ� �߻���Ų��.
        //			(4) �߰� ���� ���� �Ϸ��ϸ� Authentication(���� ����, ���� ����)��
        //				AuthenticationManager���� �����Ѵ�.
        //			(5) AuthenticationManager�� Filter���� �����ϰ�,
        //				Filter�� �� ������ ���������� ����� �� �ְ� SecurityContext�� �����Ѵ�.
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        return authentication;
    }

    // JWT Token �����ؼ� response�� ����ش�.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
    	
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>  JwtAuthenticationFilter.java  78:"+response);

    	AuthUserDetails authUserDetails = (AuthUserDetails) authResult.getPrincipal();

    	// withSubject	:	jwt�� �̸��� ���Ѵ�.
    	// withExpireAt	:	jwt ����ð��� �����Ѵ�. �������� ������ �⺻������ �������� jwt�� �ȴ�.
    	// withClaim	:	jwt�� payload �κп��� private�� �����Ѵ�.
    	//					private�� �̸��� �� ������ ������� �� �ִ�.
    	//					���⿡ �����̸��� �־ �̸� ������� �����ĺ��� �����Ѵ�.
    	// sign			:	� �ؽ� �˰������� �ؽø� �ϴ���, � ��ũ��Ű�� ����ϴ��� �����Ѵ�. 
        String jwtToken = JWT.create()
                .withSubject(authUserDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("username", authUserDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);

        Map<String, Object> data = new HashMap<>();
		
		
		data.put("jwt", String.valueOf(jwtToken));
		data.put("roles", authUserDetails.getRoles());
		data.put("fullname", URLEncoder.encode(authUserDetails.getFullname(), "UTF-8") );
		

		ObjectMapper objectMapper = new ObjectMapper();
		// {"jwt":"~~~", "roles":"USER_ROLES"}
        String result = objectMapper.writeValueAsString(data);
        // /login ��ġ�� ����, ��ū�� body�� ������
        response.getWriter().write(result);
    }
}
