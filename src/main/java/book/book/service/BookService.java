package book.book.service;

import java.util.List;

import book.book.dto.BookDto;

public interface BookService {
	
	List<BookDto> selectBookList(String searchWord) throws Exception;
	
	void insertBook(BookDto bookOne) throws Exception;

	void updateBook(BookDto book) throws Exception;

	void deleteBook(List<BookDto> bookList) throws Exception;
}
