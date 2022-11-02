package ru.msugrobov.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.msugrobov.DTO.PlayerDTO;
import ru.msugrobov.entities.Role;
import ru.msugrobov.exceptions.CredentialsErrorException;
import ru.msugrobov.exceptions.LoginAlreadyExistsException;
import ru.msugrobov.repositories.DBconnection;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.impl.PlayerServiceInterfaceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class RegistrationServlet extends HttpServlet {

    private final PlayerRepository playerRepository = new PlayerRepository();
    private final PlayerServiceInterfaceImpl playerServiceInterfaceImpl = new PlayerServiceInterfaceImpl
            (playerRepository);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter output = response.getWriter();
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        PlayerDTO playerDTO = new PlayerDTO(firstName, lastName, login, password, Role.USER);
        try {
            Class.forName("org.postgresql.Driver");
            DBconnection.getConnection();
            playerServiceInterfaceImpl.createPlayer(playerDTO);
        } catch (LoginAlreadyExistsException e) {
            output.println(String.format("Account with login %s already exists, try another login", login));
        } catch (CredentialsErrorException e) {
            output.println("All fields in registration form are required, try again");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
