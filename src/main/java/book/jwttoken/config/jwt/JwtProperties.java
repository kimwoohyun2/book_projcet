package book.jwttoken.config.jwt;

public interface JwtProperties {
    // ������ �˰��ִ� ��ũ��Ű
    String SECRET = "woohyun198woohyun94kim35soccer448monitor";
    int EXPIRATION_TIME = 60000 * 1440; // 1440��(24�ð�)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

}
