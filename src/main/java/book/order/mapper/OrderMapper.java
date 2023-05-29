package book.order.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import book.order.dto.RequestOrderDto;

@Mapper
public interface OrderMapper {
	List<RequestOrderDto> selectBookOrderHistory(String username) throws Exception;
	String selectMaxOrderNo() throws Exception;
	
	void insertOrder(RequestOrderDto orderOne) throws Exception;
	void insertOrderDetail(List<RequestOrderDto> orderList) throws Exception;
}
