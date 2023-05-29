package book.order.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import book.order.dto.RequestOrderDto;
import book.order.mapper.OrderMapper;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Override
	public List<RequestOrderDto> selectBookOrderHistory(String username) throws Exception {
		return orderMapper.selectBookOrderHistory(username);
	}
	
	@Override
	public String selectMaxOrderNo() throws Exception {
		return orderMapper.selectMaxOrderNo();
	}
	
	@Override
	public void insertOrder(List<RequestOrderDto> orderList) throws Exception {
		orderMapper.insertOrder(orderList.get(0));
		String rtnOrderNo = orderMapper.selectMaxOrderNo();
		for (RequestOrderDto item : orderList) {
			item.setOrderNo(Integer.parseInt(rtnOrderNo));
		}
		orderMapper.insertOrderDetail(orderList);
	}
	
}
