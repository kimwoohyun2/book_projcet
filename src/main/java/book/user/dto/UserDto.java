package book.user.dto;

import com.sun.istack.NotNull;

import lombok.Getter;

@Getter
public class UserDto {
	
    @NotNull
    private String username;	// �̸����ּ�
    
    @NotNull
    private String password;	// ��й�ȣ
    
    @NotNull
    private String fullname;	// �̸�
    
    @NotNull
    private String birthdate;	// �������
    
    private String roles;		// ����
    
    private String adminYn;		// �����ڱ��� ��ȯ����
    
    @NotNull
    private String address;		// ���ּ�
    
    private String interest;	// ���ɻ�
}