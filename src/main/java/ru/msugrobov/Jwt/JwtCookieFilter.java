package ru.msugrobov.Jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Validates given JWT
 */
public class JwtCookieFilter {

    private final JwtManager jwtManager = new JwtManager();
    private static final Integer STATUS_CODE_ACCESS_DENIED = 403;

    /**
     * Validates given JWT
     *
     * @param request HTTP request from a client
     * @return role if JWT is valid
     */
    public String jwtCookieFilter(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String jwt;

        for (Cookie aCookie : cookies) {
            String name = aCookie.getName();

            if (name.equals("Authorization")) {
                jwt = aCookie.getValue();
                try {
                    Jws<Claims> claimsJws = jwtManager.parseToken(jwt);
                    return (String) claimsJws.getBody().get("role");
                } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                         IllegalArgumentException e) {
                    response.setStatus(STATUS_CODE_ACCESS_DENIED);
                }
            }
        }
        return null;
    }
}