package ru.msugrobov.services.impl;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;

import bsh.util.JConsole;
import bsh.util.GUIConsoleInterface;
import ru.msugrobov.entities.Role;
import ru.msugrobov.entities.Type;
import ru.msugrobov.exceptions.*;
import ru.msugrobov.services.UserInterfaceService;

import javax.swing.*;

/**
 * UserInterfaceService
 * {@inheritDoc}
 */
public class UserInterfaceServiceImpl implements UserInterfaceService {
    private final PlayerServiceInterfaceImpl playerServiceInterfaceImpl;
    private final WalletServiceInterfaceImpl walletServiceInterfaceImpl;
    private final TransactionServiceInterfaceImpl transactionServiceInterfaceImpl;
    private final JFrame frame = new JFrame("Wallet service");
    private final JConsole console = new JConsole();
    private final Reader input = console.getIn();
    private final BufferedReader bufferInput = new BufferedReader(input);

    public UserInterfaceServiceImpl(PlayerServiceInterfaceImpl playerServiceInterfaceImpl,
                                    WalletServiceInterfaceImpl walletServiceInterfaceImpl,
                                    TransactionServiceInterfaceImpl transactionServiceInterfaceImpl) {
        this.playerServiceInterfaceImpl = playerServiceInterfaceImpl;
        this.walletServiceInterfaceImpl = walletServiceInterfaceImpl;
        this.transactionServiceInterfaceImpl = transactionServiceInterfaceImpl;
    }

    /**
     * Start user interface
     */
    public void start() {

        frame.add(console);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        frame.setVisible(true);

        inputLoop(console);

        System.exit(0);
    }

    private void inputLoop(GUIConsoleInterface console) {
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
                        walletServiceLoop(console);
                        break;
                    case ("transaction"):
                    case ("3"):
                        transactionServiceLoop(console);
                        break;
                    case ("command"):
                    case ("4"):
                        console.print("future logic of commandService");
                        break;
                    case ("menu"):
                        console.print("Type entity to work with:" + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    case (";"):
                        inputLoop(console);
                    default:
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
        String newLine = System.getProperty("line.separator");
        String viableInputParameters = "1. Create player" + newLine +
                "2. Find player" + newLine +
                "3. Find all players in storage" + newLine +
                "4. Update player" + newLine +
                "5. Delete player" + newLine +
                "(type 'back' to return to the main menu): ";
        console.print("Type command: " + newLine, Color.BLACK);
        console.print(viableInputParameters, Color.BLACK);

        String line;
        try {
            while ((line = bufferInput.readLine()) != null) {
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
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find"):
                    case ("2"):
                        console.print("Enter id: ", Color.BLACK);
                        try {
                            console.print(playerServiceInterfaceImpl
                                    .findById(Integer.parseInt(bufferInput.readLine())) + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find all"):
                    case ("3"):
                        console.print(playerServiceInterfaceImpl.findAllPlayers() + newLine, Color.BLACK);
                        console.print("Press ENTER to return to the menu ", Color.BLACK);
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
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("delete"):
                    case ("5"):
                        console.print("Enter id to delete player: ");
                        try {
                            playerServiceInterfaceImpl.deletePlayer(Integer.parseInt(bufferInput.readLine()));
                            console.print("Player successfully deleted" + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("menu"):
                        console.print("Type command: " + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    case ("back"):
                        inputLoop(console);
                    case (";"):
                        playerServiceLoop(console);
                    default:
                        console.print(String.format("Command %s does not exist, try again:  " +
                                newLine + viableInputParameters + newLine, line), Color.BLACK);
                        console.print("Type command: ", Color.BLACK);
                }
            }
            bufferInput.close();
        } catch (IOException | IllegalArgumentException exception) {
            console.print("Invalid input, press ENTER to return to the menu ", Color.RED);
        }
    }

    private void walletServiceLoop(GUIConsoleInterface console) {
        String newLine = System.getProperty("line.separator");
        String viableInputParameters = "1. Create wallet" + newLine +
                "2. Find wallet" + newLine +
                "3. Find all wallets in storage" + newLine +
                "4. Update wallet" + newLine +
                "5. Delete wallet" + newLine +
                "(type 'back' to return to the main menu): ";
        console.print("Type command: " + newLine, Color.BLACK);
        console.print(viableInputParameters, Color.BLACK);

        String line;
        try {
            while ((line = bufferInput.readLine()) != null) {
                switch (line.toLowerCase()) {
                    case ("quit"):
                        bufferInput.close();
                        break;
                    case ("create"):
                    case ("1"):
                        console.print("Creating new wallet" + newLine, Color.BLACK);
                        console.print("Enter id: ", Color.BLACK);
                        Integer idNumber = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter id of the player who owns the wallet: ", Color.BLACK);
                        int playerId = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter initial wallet balance: ", Color.BLACK);
                        BigDecimal balance = new BigDecimal(Integer.parseInt(bufferInput.readLine()));
                        try {
                            walletServiceInterfaceImpl.createWallet
                                    (idNumber, playerId, balance);
                            console.print("Wallet successfully created" + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (PlayerIdAlreadyExistsException | IdAlreadyExistsException | IdNotFoundException
                                exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find"):
                    case ("2"):
                        console.print("Enter id: ", Color.BLACK);
                        try {
                            console.print(walletServiceInterfaceImpl
                                    .findById(Integer.parseInt(bufferInput.readLine())) + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find all"):
                    case ("3"):
                        console.print(walletServiceInterfaceImpl.findAllWallets() + newLine, Color.BLACK);
                        console.print("Press ENTER to return to the menu ", Color.BLACK);
                        break;
                    case ("update"):
                    case ("4"):
                        console.print("Updating wallet" + newLine, Color.BLACK);
                        console.print("Enter id of the wallet to be updated: ", Color.BLACK);
                        Integer idWalletToBeUpdated = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter new wallet balance: ", Color.BLACK);
                        BigDecimal newBalance = new BigDecimal(Integer.parseInt(bufferInput.readLine()));
                        try {
                            walletServiceInterfaceImpl.updateWallet(idWalletToBeUpdated, newBalance);
                            console.print("Wallet successfully updated" + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("delete"):
                    case ("5"):
                        console.print("Enter id to delete wallet: ");
                        try {
                            walletServiceInterfaceImpl.deleteWallet(Integer.parseInt(bufferInput.readLine()));
                            console.print("Wallet successfully deleted" + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("menu"):
                        console.print("Type command: " + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    case ("back"):
                        inputLoop(console);
                    case (";"):
                        walletServiceLoop(console);
                    default:
                        console.print(String.format("Command %s does not exist, try again:  " +
                                newLine + viableInputParameters + newLine, line), Color.BLACK);
                        console.print("Type command: ", Color.BLACK);
                }
            }
            bufferInput.close();
        } catch (IOException | IllegalArgumentException exception) {
            console.print("Invalid input, press ENTER to return to the menu ", Color.RED);
        }
    }

    private void transactionServiceLoop(GUIConsoleInterface console) {
        String newLine = System.getProperty("line.separator");
        String viableInputParameters = "1. Create transaction" + newLine +
                "2. Find transaction" + newLine +
                "3. Find all transactions in storage" + newLine +
                "4. Find by wallet id" + newLine +
                "5. Update transaction" + newLine +
                "6. Delete transaction" + newLine +
                "(type 'back' to return to the main menu): ";
        console.print("Type command: " + newLine, Color.BLACK);
        console.print(viableInputParameters, Color.BLACK);

        String line;
        try {
            while ((line = bufferInput.readLine()) != null) {
                switch (line.toLowerCase()) {
                    case ("quit"):
                        bufferInput.close();
                        break;
                    case ("create"):
                    case ("1"):
                        console.print("Creating new transaction" + newLine, Color.BLACK);
                        console.print("Enter id: ", Color.BLACK);
                        Integer idNumber = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter id of the wallet: ", Color.BLACK);
                        int walletId = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter type DEBIT or CREDIT: ", Color.BLACK);
                        Type type = Type.valueOf(bufferInput.readLine().toUpperCase());
                        console.print("Enter value of the transaction: ", Color.BLACK);
                        BigDecimal value = new BigDecimal(Integer.parseInt(bufferInput.readLine()));
                        try {
                            transactionServiceInterfaceImpl.createTransaction
                                    (idNumber, walletId, type, value);
                            console.print("Transaction successfully created" + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdAlreadyExistsException | IdNotFoundException |
                                 InsufficientBalanceException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find"):
                    case ("2"):
                        console.print("Enter id: ", Color.BLACK);
                        try {
                            console.print(transactionServiceInterfaceImpl
                                    .findById(Integer.parseInt(bufferInput.readLine())) + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find all"):
                    case ("3"):
                        console.print(transactionServiceInterfaceImpl.findAllTransactions() + newLine, Color.BLACK);
                        console.print("Press ENTER to return to the menu ", Color.BLACK);
                        break;
                    case ("find by wallet"):
                    case ("4"):
                        console.print("Enter wallet id: ", Color.BLACK);
                        try {
                            console.print(transactionServiceInterfaceImpl
                                    .findAllTransactionsByWalletId(Integer.parseInt(bufferInput.readLine())) + 
                                    newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("update"):
                    case ("5"):
                        console.print("Updating transaction" + newLine, Color.BLACK);
                        console.print("Enter id of the transaction to be updated: ", Color.BLACK);
                        Integer idTransactionToBeUpdated = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter new transaction value: ", Color.BLACK);
                        BigDecimal newValue = new BigDecimal(Integer.parseInt(bufferInput.readLine()));
                        try {
                            transactionServiceInterfaceImpl.updateTransaction(idTransactionToBeUpdated, newValue);
                            console.print("Transaction successfully updated" + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException | InsufficientBalanceException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("delete"):
                    case ("6"):
                        console.print("Enter id to delete transaction: ");
                        try {
                            transactionServiceInterfaceImpl.deleteTransaction(Integer.parseInt(bufferInput.readLine()));
                            console.print("Transaction successfully deleted" + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("menu"):
                        console.print("Type command: " + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    case ("back"):
                        inputLoop(console);
                    case (";"):
                        transactionServiceLoop(console);
                    default:
                        console.print(String.format("Command %s does not exist, try again:  " +
                                newLine + viableInputParameters + newLine, line), Color.BLACK);
                        console.print("Type command: ", Color.BLACK);
                }
            }
        } catch (IOException | IllegalArgumentException exception) {
            console.print("Invalid input, press ENTER to return to the menu ", Color.RED);
        }
    }
}