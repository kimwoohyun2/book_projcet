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

// EnableWebSecurity �� ������ ��ť��Ƽ�� Ȱ��ȭ�����ش�.
//   -> Ȱ��ȭ�Ǹ� �⺻ ������ ����ü�ο� ����� �ǰ� �Ǵµ� 
//      ��ť��Ƽ ���Ͱ� �⺻ ������ ����ü�κ��� ���� ����ȴٴ� ���� ������ ��.
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder encoder() {
        // DB �н����� ��ȣȭ
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    	DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }
    
    // ��ť��Ƽ�� Ȱ��ȭ�Ǹ� ��� ��û�� ���� ��Ʈ�ѷ��� ���� ���� �� configure �޼ҵ尡 ����ȴ�.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http
        		// CorsConfig.java���� cors ������ �͵��� Ȱ��ȭ��Ų��.
        		.cors().and()
        		// SpringSecurity�� default�� ����Ǿ��ִ� csrf protection�� �����Ѵ�.
                .csrf().disable()
                // JWT�� ����� ���̱� ������ STATELESS ��, ������ ������� �ʰڴٴ� �����̴�.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // Spring Security�� �⺻������ �����ϴ� formLogin ����� ������� �ʰڴٴ°��� ��Ÿ����.
                .formLogin().disable()
                // �� ��û���� id, pwd�� ������ ������� �����ϴ� httpBasic�� ������� �ʰڴٴ°��� ��Ÿ����.
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                // authorizeRequests() �����δ� � ��û���� ��� ó������ �����ϴ� �κ��̴�.
                .authorizeRequests()
                .antMatchers("/", "/**").permitAll();
                // "/api/v1/user"�� �����ϴ� ��� ��û�� ���� ROLE_USER �Ǵ� ROLE_MANAGER �Ǵ� ROLE_ADMIN ������ �־�߸� �����ϰڴٴ� �����̴�.
//                .antMatchers("/api/v1/user/**")
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll();
    }
}
