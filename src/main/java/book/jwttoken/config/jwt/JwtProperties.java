package book.jwttoken.config.jwt;

public interface JwtProperties {
    // 서버만 알고있는 시크릿키
    String SECRET = "woohyun198woohyun94kim35soccer448monitor";
    int EXPIRATION_TIME = 60000 * 1440; // 1440분(24시간)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

}
