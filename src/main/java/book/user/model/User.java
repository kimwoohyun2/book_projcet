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
	// �̸����ּ�
	@Id
    private String username;
	// ��й�ȣ
    private String password;
    // �̸�
    private String fullname;
    // �������
    private String birthdate;
    // ����
    private String roles;	// ROLE_USER, ROLE_ADMIN
    // �����ڱ��� ��ȯ����
    private String adminYn;	// 'Y':������ ��ȯ��. 'N':������ ����ȯ, (����) default: 'N'
    // ���ּ�
    private String address;
    // ���ɻ�
    private String interest;

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
