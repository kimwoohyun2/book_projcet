package book.order.dto;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestOrderDto {
	@NotNull
	private Integer orderNo;		// �ֹ� NO
	
	@NotNull
	private Integer orderDetailNo;	// �ֹ��� NO
	
    @NotNull
    private String userUsername;	// ȸ�� �̸����ּ�
    
    @NotNull
    private String bookNo;			// ���� NO
    
    @NotNull
    private String bookTitle;		// ���� ����
    
    @NotNull
    private Integer orderPrice;		// �ֹ�����
    
    @NotNull
    private String orderDate;		// �ֹ��Ͻ�
}