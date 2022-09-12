package ru.msugrobov.services.impl;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import bsh.util.JConsole;
import bsh.util.GUIConsoleInterface;
import ru.msugrobov.entities.Role;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.exceptions.LoginAlreadyExistsException;
import ru.msugrobov.services.UserInterfaceService;

import javax.swing.*;

/**
 * UserInterfaceService
 * {@inheritDoc}
 */
public class UserInterfaceServiceImpl implements UserInterfaceService {
    private final PlayerServiceInterfaceImpl playerServiceInterfaceImpl;

    public UserInterfaceServiceImpl(PlayerServiceInterfaceImpl playerServiceInterfaceImpl) {
        this.playerServiceInterfaceImpl = playerServiceInterfaceImpl;
    }

    /**
     * Start user interface
     */
    public void start() {

        JFrame frame = new JFrame("Wallet service");
        JConsole console = new JConsole();

        frame.add(console);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        frame.setVisible(true);

        inputLoop(console);

        System.exit(0);
    }

    private void inputLoop(GUIConsoleInterface console) {
        Reader input = console.getIn();
        BufferedReader bufferInput = new BufferedReader(input);

        String newLine = System.getProperty("line.separator");
        String viableInputParameters = "1. Player" + newLine +
                "2. Wallet" + newLine +
                "3. Transaction" + newLine +
                "4. Command" + newLine +
                "(type 'quit' to exit): ";

        console.print("Type entity to work with:" + newLine, Color.BLACK);
        console.print(viableInputParameters, Color.BLACK);

        String line;
        try {
            while ((line = bufferInput.readLine()) != null) {
                console.print("You typed: " + line + newLine, Color.BLUE);
                switch (line.toLowerCase()) {
                    case ("quit"):
                        bufferInput.close();
                        break;
                    case ("player"):
                    case ("1"):
                        playerServiceLoop(console);
                        break;
                    case ("wallet"):
                    case ("2"):
                        console.print("future logic of walletService");
                        break;
                    case ("transaction"):
                    case ("3"):
                        console.print("future logic of transactionService");
                        break;
                    case ("command"):
                    case ("4"):
                        console.print("future logic of commandService");
                        break;
                    case ("menu"):
                        console.print("Type entity to work with:" + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    default:
                        console.print("Type entity to work with:" + newLine, Color.BLACK);
                        console.print(String.format("Entity %s does not exist, try again:  " +
                                newLine + viableInputParameters, line), Color.BLACK);
                }
            }
            bufferInput.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void playerServiceLoop(GUIConsoleInterface console) {
        Reader input = console.getIn();
        BufferedReader bufferInput = new BufferedReader(input);

        String newLine = System.getProperty("line.separator");
        String viableInputParameters = "1. Create player" + newLine +
                "2. Find player" + newLine +
                "3. Find all players from storage" + newLine +
                "4. Update player" + newLine +
                "5. Delete player" + newLine +
                "(type 'back' to return to the main menu): ";
        console.print("Type command: " + newLine, Color.BLACK);
        console.print(viableInputParameters, Color.BLACK);

        String line;
        try {
            while ((line = bufferInput.readLine()) != null) {
                console.print("You typed: " + line + newLine, Color.BLUE);
                switch (line.toLowerCase()) {
                    case ("quit"):
                        bufferInput.close();
                        break;
                    case ("create"):
                    case ("1"):
                        console.print("Creating new player" + newLine, Color.BLACK);
                        console.print("Enter id: ", Color.BLACK);
                        int idNumber = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter first name: ", Color.BLACK);
                        String firstName = bufferInput.readLine();
                        console.print("Enter last name: ", Color.BLACK);
                        String lastName = bufferInput.readLine();
                        console.print("Enter login: ", Color.BLACK);
                        String login = bufferInput.readLine();
                        console.print("Enter password: ", Color.BLACK);
                        String password = bufferInput.readLine();
                        console.print("Enter role ADMIN or USER: ", Color.BLACK);
                        Role role = Role.valueOf(bufferInput.readLine().toUpperCase());
                        try {
                            playerServiceInterfaceImpl.createPlayer
                                    (idNumber, firstName, lastName, login, password, role);
                            console.print("Player successfully created" + newLine, Color.BLACK);
                            console.print("Press any key to return to the menu ", Color.BLACK);
                        } catch (IdAlreadyExistsException | LoginAlreadyExistsException exception) {
                            console.print(exception.getMessage() + newLine, Color.BLACK);
                            console.print("Press any key to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find"):
                    case ("2"):
                        console.print("Enter id: ", Color.BLACK);
                        try {
                            console.print(playerServiceInterfaceImpl
                                    .findById(Integer.parseInt(bufferInput.readLine())) + newLine, Color.BLACK);
                            console.print("Press any key to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press any key to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find all"):
                    case ("3"):
                        console.print(playerServiceInterfaceImpl.findAllPlayers() + newLine, Color.BLACK);
                        console.print("Press any key to return to the menu ", Color.BLACK);
                        break;
                    case ("update"):
                    case ("4"):
                        console.print("Updating player" + newLine, Color.BLACK);
                        console.print("Enter id of the player to be updated: ", Color.BLACK);
                        int idPlayerToBeUpdated = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter first name: ", Color.BLACK);
                        String updatedFirstName = bufferInput.readLine();
                        console.print("Enter last name: ", Color.BLACK);
                        String updatedLastName = bufferInput.readLine();
                        console.print("Enter password: ", Color.BLACK);
                        String updatedPassword = bufferInput.readLine();
                        try {
                            playerServiceInterfaceImpl.updatePlayer
                                    (idPlayerToBeUpdated, updatedFirstName, updatedLastName, updatedPassword);
                            console.print("Player successfully updated" + newLine, Color.BLACK);
                            console.print("Press any key to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.BLACK);
                            console.print("Press any key to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("delete"):
                    case ("5"):
                        console.print("Enter id to delete player: ");
                        try {
                            playerServiceInterfaceImpl.deletePlayer(Integer.parseInt(bufferInput.readLine()));
                            console.print("Player successfully deleted" + newLine, Color.BLACK);
                            console.print("Press any key to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press any key to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("menu"):
                        console.print("Type command: " + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    case ("back"):
                        inputLoop(console);
                    default:
                        console.print(String.format("Command %s does not exist, try again:  " +
                                newLine + viableInputParameters + newLine, line), Color.BLACK);
                        console.print("Type command: ", Color.BLACK);
                }
            }
            bufferInput.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}