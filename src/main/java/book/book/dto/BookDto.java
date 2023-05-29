package book.book.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BookDto {
	// 도서 NO
	private int bookNo;
	// 도서 제목
	private String bookTitle;
	// 도서 설명
	private String bookDesc;
	// 도서 이미지주소
	private String bookImage;
	// 도서 저자
	private String bookAuthor;
	// 도서 출판사
	private String bookPublisher;
	// 도서 출판일
	private String bookPubDate;
	// 도서 페이지수
	private String bookPageNo;
	// 도서 가격
	private String bookPrice;
	// 등록일시
	private LocalDateTime regiDate;
	// 최종수정일시
	private LocalDateTime lastDate;
}
