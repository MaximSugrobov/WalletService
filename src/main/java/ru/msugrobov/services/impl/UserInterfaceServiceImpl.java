package ru.msugrobov.services.impl;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import bsh.util.JConsole;
import bsh.util.GUIConsoleInterface;
import ru.msugrobov.entities.Player;
import ru.msugrobov.entities.Role;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.UserInterfaceService;

import javax.swing.*;

/**
 * UserInterfaceService
 * {@inheritDoc}
 */
public class UserInterfaceServiceImpl implements UserInterfaceService {

    /**
     * Start user interface
     */
    public void start() {

        JFrame frame = new JFrame("Wallet service");
        JConsole console = new JConsole();

        frame.add(console);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);

        frame.setVisible(true);

        inputLoop(console);

        System.exit(0);
    }

    private static void inputLoop(GUIConsoleInterface console) {
        Reader input = console.getIn();
        BufferedReader bufferInput = new BufferedReader(input);

        String newLine = System.getProperty("line.separator");
        String viableInputParameters = "1. Player" + newLine +
                "2. Wallet" + newLine +
                "3. Transaction" + newLine +
                "4. Command:" + newLine +
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
                    case ("player"): case ("1"):
                        console.print("future logic of playerService " + newLine);
                        console.print("Enter id: ");
                        // Testing in runtime
                        PlayerRepository playerRepository =
                                new PlayerRepository(new ArrayList<>());
                        PlayerServiceInterfaceImpl playerServiceInterface =
                                new PlayerServiceInterfaceImpl(playerRepository);
                        Player player = new Player(1, "Max",
                                "Snow", "Max", "pass", Role.ADMIN);
                        playerRepository.create(player);
                        int idNumber = Integer.parseInt(bufferInput.readLine());
                        playerServiceInterface.findById(idNumber);
                        console.print(playerServiceInterface.findById(idNumber));
                        break;
                    case ("wallet"): case ("2"):
                        console.print("future logic of walletService");
                        break;
                    case ("transaction"): case ("3"):
                        console.print("future logic of transactionService");
                        break;
                    case ("command"): case ("4"):
                        console.print("future logic of commandService");
                        break;
                    case ("menu"):
                        console.print("Type entity to work with:" + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    default:
                        console.print(String.format("Entity %s does not exist, try again:  ", line), Color.BLACK);
                    }
                }
            bufferInput.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}