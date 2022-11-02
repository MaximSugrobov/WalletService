package ru.msugrobov.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.msugrobov.Jwt.JwtManager;
import ru.msugrobov.entities.Player;
import ru.msugrobov.repositories.DBconnection;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.impl.PlayerServiceInterfaceImpl;

import java.io.IOException;
import java.sql.*;

/**
 * Describes login contract
 */
public class LoginServlet extends HttpServlet {

    private final PlayerRepository playerRepository = new PlayerRepository();
    private final PlayerServiceInterfaceImpl playerServiceInterfaceImpl = new PlayerServiceInterfaceImpl
            (playerRepository);
    private final JwtManager jwtManager = new JwtManager();
    private static final String SELECT_PLAYER_BY_LOGIN_AND_PASSWORD =
            "SELECT * FROM players WHERE login=? and password=?";

    /**
     * Performs login process
     *
     * @param request HTTP request from a client
     * @param response HTTP response form a server
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DBconnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BY_LOGIN_AND_PASSWORD);
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next() && resultSet.getString("password").equals(password)
                    && resultSet.getString("login").equals(login)) {
                Player playerForJwt = playerServiceInterfaceImpl.findById(resultSet.getInt("id"));
                String jwt = jwtManager.createToken(playerForJwt);
                Cookie cookie = new Cookie("Authorization", jwt);
                response.addCookie(cookie);
                request.getRequestDispatcher("/WEB-INF/login_success.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/login_failure.jsp").forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
