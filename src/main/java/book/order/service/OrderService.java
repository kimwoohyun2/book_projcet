package book.order.service;

import java.util.List;
import book.order.dto.RequestOrderDto;

public interface OrderService {
	public List<RequestOrderDto> selectBookOrderHistory(String username) throws Exception;
	public String selectMaxOrderNo() throws Exception;
	void insertOrder(List<RequestOrderDto> orderList) throws Exception;
}
