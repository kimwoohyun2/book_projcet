package book.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import book.user.dto.UserDto;

@Mapper
public interface UserMapper {
	void insertUser(UserDto user) throws Exception;
}
