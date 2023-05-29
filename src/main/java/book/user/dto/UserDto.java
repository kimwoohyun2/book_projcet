package book.user.dto;

import com.sun.istack.NotNull;

import lombok.Getter;

@Getter
public class UserDto {
	
    @NotNull
    private String username;	// 이메일주소
    
    @NotNull
    private String password;	// 비밀번호
    
    @NotNull
    private String fullname;	// 이름
    
    @NotNull
    private String birthdate;	// 생년월일
    
    private String roles;		// 권한
    
    private String adminYn;		// 관리자권한 전환여부
    
    @NotNull
    private String address;		// 집주소
    
    private String interest;	// 관심사
}