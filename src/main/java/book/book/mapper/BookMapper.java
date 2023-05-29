package book.book.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import book.book.dto.BookDto;

@Mapper
public interface BookMapper {
	List<BookDto> selectBookList(String searchWord) throws Exception;
	
	void insertBook(BookDto bookOne) throws Exception;

	void updateBook(BookDto book) throws Exception;
	
	void deleteBook(List<BookDto> bookList) throws Exception;
}
