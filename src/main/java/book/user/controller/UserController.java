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
        
        // �Ʒ����� jpa�� save�޼ҵ带 ��������� ���� pk �ߺ� �μ�Ʈ �� ������ �߻���Ű�� �� �ϹǷ�
        // ���⿡�� �̸� pk�� �ߺ� ���θ� Ȯ�����ش�.
        User returnUser = userRepository.findByUsername(user.getUsername());
        
        if (returnUser != null) {
        	// �̹� ���Ե� username�� ���
        	return "already";
        }
        
        // jpa�� save �޼ҵ�� �̹� pk���� �־ �ߺ� �μ�Ʈ�� �� ��� ������ �߻���Ű�� �ʰ�
        // update ó���� �ϹǷ� ������ ��.
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
