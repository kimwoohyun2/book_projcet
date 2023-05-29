package book.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
	// 이메일주소
	@Id
    private String username;
	// 비밀번호
    private String password;
    // 이름
    private String fullname;
    // 생년월일
    private String birthdate;
    // 권한
    private String roles;	// ROLE_USER, ROLE_ADMIN
    // 관리자권한 전환여부
    private String adminYn;	// 'Y':관리자 전환됨. 'N':관리자 미전환, (참고) default: 'N'
    // 집주소
    private String address;
    // 관심사
    private String interest;

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
