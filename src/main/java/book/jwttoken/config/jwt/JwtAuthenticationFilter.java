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

// 인증 (신원 확인 절차)
//   -> JwtAuthenticationFilter는 인증단계를 처리하는 필터로써
//		spring security에서 인증의 역할을 하는 UsernamePasswordAuthenticationFilter를 상속받아 구현한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;

    // Authentication 객체를 만들어서 리턴한다.
    // client가 POST: /login 요청을 하면 attemptAuthentication 메소드가 실행된다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
    	
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>  JwtAuthenticationFilter.java  42:"+request);

        // 1. ObjectMapper를 이용해 request에 담겨있는 사용자 정보를 DTO객체(userDto)로 만든다.
        ObjectMapper om = new ObjectMapper();
        UserDto userDto = null;
        try {
        	userDto = om.readValue(request.getInputStream(), UserDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. userDto정보를 spring security에서 이용할 수 있게 token화 시키고
        //    해당 token으로 authenticationManager.authenticate를 이용하여 인증을 진행하게 된다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                		userDto.getUsername(),
                		userDto.getPassword());

        // 3. 아래의 authenticate() 함수가 호출 되면 
        //       -> (1) UserDetailSService(스프링 시큐리티에 내장된 interface)를 재정의한 AuthUserDetails에 있는
        //				loadUserByUsername를 인증 프로바이더가 호출하여 DB에서 사용자를 조회해온다.
        //			(2)	만약 사용자가 없을 경우 UserNotFoundException 예외를 발생시키고
        //				성공하면 UserDetails 객체를 반환한다.
        //			(3) 반환 받은 UserDetails 객체에 저장된 Password(DB값)와
        //				로그인시 입력한 Password를
        //				(Authentication에 저장된 Password, 토큰의 두번째 파라미터(credential))
        //				matches 메서드로 비교. 일치하지 않는다면 BadCredentialException 예외를 발생시킨다.
        //			(4) 추가 검증 까지 완료하면 Authentication(유저 정보, 권한 정보)를
        //				AuthenticationManager에게 전달한다.
        //			(5) AuthenticationManager는 Filter에게 전달하고,
        //				Filter는 이 정보를 전역적으로 사용할 수 있게 SecurityContext에 전달한다.
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        return authentication;
    }

    // JWT Token 생성해서 response에 담아준다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
    	
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>  JwtAuthenticationFilter.java  78:"+response);

    	AuthUserDetails authUserDetails = (AuthUserDetails) authResult.getPrincipal();

    	// withSubject	:	jwt의 이름을 정한다.
    	// withExpireAt	:	jwt 만료시간을 지정한다. 설정하지 않으면 기본적으로 무한지속 jwt가 된다.
    	// withClaim	:	jwt의 payload 부분에서 private을 설정한다.
    	//					private의 이름과 그 내용을 적어넣을 수 있다.
    	//					여기에 유저이름을 넣어서 이를 기반으로 유저식별을 진행한다.
    	// sign			:	어떤 해싱 알고리즘으로 해시를 하는지, 어떤 시크릿키를 사용하는지 결정한다. 
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
        // /login 마치고 나면, 토큰을 body로 보내줌
        response.getWriter().write(result);
    }
}
