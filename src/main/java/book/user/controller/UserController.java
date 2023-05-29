package book.user.controller;

import book.user.model.User;
import book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins="*", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @PostMapping("/v1/join")
    public String join(@RequestBody User user) {
    	System.out.println(">>>>>>>>>>>>  controller.java  64:" + user);
    	
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        
        // 아래에서 jpa의 save메소드를 사용함으로 인해 pk 중복 인서트 시 에러를 발생시키게 못 하므로
        // 여기에서 미리 pk값 중복 여부를 확인해준다.
        User returnUser = userRepository.findByUsername(user.getUsername());
        
        if (returnUser != null) {
        	// 이미 가입된 username인 경우
        	return "already";
        }
        
        // jpa의 save 메소드는 이미 pk값이 있어서 중복 인서트가 될 경우 에러를 발생시키지 않고
        // update 처리를 하므로 유의할 것.
        userRepository.save(user);
        
        return "success";
    }
    
    @PostMapping("/register")
    public String register(@RequestBody User user) {
    	System.out.printf(">>>>>>>>>>>>  controller.java  81:", user);
    	
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        
        userRepository.save(user);
        
        return "success";
    }
}
