package book.order.dto;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestOrderDto {
	@NotNull
	private Integer orderNo;		// 주문 NO
	
	@NotNull
	private Integer orderDetailNo;	// 주문상세 NO
	
    @NotNull
    private String userUsername;	// 회원 이메일주소
    
    @NotNull
    private String bookNo;			// 도서 NO
    
    @NotNull
    private String bookTitle;		// 도서 제목
    
    @NotNull
    private Integer orderPrice;		// 주문가격
    
    @NotNull
    private String orderDate;		// 주문일시
}