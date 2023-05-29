package book.book.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BookDto {
	// ���� NO
	private int bookNo;
	// ���� ����
	private String bookTitle;
	// ���� ����
	private String bookDesc;
	// ���� �̹����ּ�
	private String bookImage;
	// ���� ����
	private String bookAuthor;
	// ���� ���ǻ�
	private String bookPublisher;
	// ���� ������
	private String bookPubDate;
	// ���� ��������
	private String bookPageNo;
	// ���� ����
	private String bookPrice;
	// ����Ͻ�
	private LocalDateTime regiDate;
	// ���������Ͻ�
	private LocalDateTime lastDate;
}
