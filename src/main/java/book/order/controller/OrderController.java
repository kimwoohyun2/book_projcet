package book.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import book.order.dto.RequestOrderDto;
import book.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/getBookOrderHistory")
	public List<RequestOrderDto> getBookOrderHistory(@RequestParam String username) throws Exception{
		
		List<RequestOrderDto> list = orderService.selectBookOrderHistory(username);
		
		return list;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/getMaxOrderNo")
	public String getMaxOrderNo() throws Exception{
		
		String rtnNo = orderService.selectMaxOrderNo();
		
		return rtnNo;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/doOrderBook")
	public String insertOrder(@RequestBody List<RequestOrderDto> orderList) throws Exception{
		
		orderService.insertOrder(orderList);
		return "success";
	}
}
