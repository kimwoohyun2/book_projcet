package book.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.book.dto.BookDto;
import book.book.mapper.BookMapper;

@Service
public class BookServiceImpl implements BookService{
	
	@Autowired
	private BookMapper bookMapper;
	
	@Override
	public List<BookDto> selectBookList(String searchWord) throws Exception {
		
		return bookMapper.selectBookList(searchWord);
	}
	
	@Override
	public void insertBook(BookDto bookOne) throws Exception {
		bookMapper.insertBook(bookOne);
	}

	@Override
	public void updateBook(BookDto book) throws Exception {
		bookMapper.updateBook(book);
	}

	@Override
	public void deleteBook(List<BookDto> bookList) throws Exception {
		bookMapper.deleteBook(bookList);
	}
}	
