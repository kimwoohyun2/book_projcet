package book.book.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import book.book.dto.BookDto;
import book.book.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {
	
	@Autowired
	private BookService bookService; 
	
	@RequestMapping(method = RequestMethod.GET, path = "/getBookList")
	public Map<String, Object> getBookList(@RequestParam String searchWord) throws Exception{
		Map<String, Object> data = new HashMap<>();
		
		List<BookDto> list = bookService.selectBookList(searchWord);
		
		list.forEach(System.out::println);
		
		data.put("bookList", list);
		
		return data;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/addBook")
	public String insertBook(@RequestBody BookDto bookOne) throws Exception{
		
		bookService.insertBook(bookOne);
		return "success";
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/updateBook")
	public String updateBook(@RequestBody BookDto book) throws Exception{
		
		bookService.updateBook(book);
		return "success";
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/deleteBook")
	public String deleteBook(@RequestBody List<BookDto> bookList) throws Exception{
		
		bookService.deleteBook(bookList);
		return "success";
	}
}
