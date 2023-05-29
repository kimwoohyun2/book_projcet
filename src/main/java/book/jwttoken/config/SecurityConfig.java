package book.jwttoken.config;

import book.jwttoken.config.jwt.JwtAuthenticationFilter;
import book.jwttoken.config.jwt.JwtAuthorizationFilter;
import book.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

// EnableWebSecurity 는 스프링 시큐리티를 활성화시켜준다.
//   -> 활성화되면 기본 스프링 필터체인에 등록이 되게 되는데 
//      시큐리티 필터가 기본 스프링 필터체인보다 먼저 실행된다는 점에 유의할 것.
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder encoder() {
        // DB 패스워드 암호화
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    	DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }
    
    // 시큐리티가 활성화되면 모든 요청에 대해 컨트롤러에 가기 전에 이 configure 메소드가 실행된다.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http
        		// CorsConfig.java에서 cors 설정한 것들을 활성화시킨다.
        		.cors().and()
        		// SpringSecurity에 default로 적용되어있는 csrf protection를 해제한다.
                .csrf().disable()
                // JWT를 사용할 것이기 때문에 STATELESS 즉, 세션을 사용하지 않겠다는 설정이다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // Spring Security가 기본적으로 제공하는 formLogin 기능을 사용하지 않겠다는것을 나타낸다.
                .formLogin().disable()
                // 매 요청마다 id, pwd를 보내는 방식으로 인증하는 httpBasic를 사용하지 않겠다는것을 나타낸다.
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                // authorizeRequests() 밑으로는 어떤 요청들을 어떻게 처리할지 설정하는 부분이다.
                .authorizeRequests()
                .antMatchers("/", "/**").permitAll();
                // "/api/v1/user"로 시작하는 모든 요청에 대해 ROLE_USER 또는 ROLE_MANAGER 또는 ROLE_ADMIN 권한이 있어야만 응답하겠다는 설정이다.
//                .antMatchers("/api/v1/user/**")
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll();
    }
}
