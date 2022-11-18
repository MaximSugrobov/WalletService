package ru.msugrobov.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.msugrobov.Jwt.JwtCookieFilter;

import java.io.IOException;

/**
 * Validates user's JWT
 */
public class AuthorizationServlet extends HttpServlet {

    JwtCookieFilter jwtCookieFilter = new JwtCookieFilter();

    /**
     * Validates user's JWT and role
     *
     * @param request HTTP request from client with JWT in cookies
     * @param response HTTP response from server with JWT in cookies
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String role = jwtCookieFilter.jwtCookieFilter(request, response);
        if (role != null) {
            switch (role) {
                case ("ADMIN"):
                    request.getRequestDispatcher("/WEB-INF/admin_ui.jsp").forward(request, response);
                    break;
                case ("USER"):
                    request.getRequestDispatcher("/WEB-INF/user_ui.jsp").forward(request, response);
                    break;
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
