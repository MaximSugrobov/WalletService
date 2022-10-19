package ru.msugrobov.services.impl;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import bsh.util.JConsole;
import bsh.util.GUIConsoleInterface;
import ru.msugrobov.ApplicationContext;
import ru.msugrobov.DTO.PlayerDTO;
import ru.msugrobov.entities.ActionResult;
import ru.msugrobov.entities.Player;
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
    private final AuditEventServiceInterfaceImpl auditEventServiceInterfaceImpl;
    private final JFrame frame = new JFrame("Wallet service");
    private final JConsole console = new JConsole();
    private final Reader input = console.getIn();
    private final BufferedReader bufferInput = new BufferedReader(input);

    public UserInterfaceServiceImpl(PlayerServiceInterfaceImpl playerServiceInterfaceImpl,
                                    WalletServiceInterfaceImpl walletServiceInterfaceImpl,
                                    TransactionServiceInterfaceImpl transactionServiceInterfaceImpl,
                                    AuditEventServiceInterfaceImpl auditEventServiceInterfaceImpl) {
        this.playerServiceInterfaceImpl = playerServiceInterfaceImpl;
        this.walletServiceInterfaceImpl = walletServiceInterfaceImpl;
        this.transactionServiceInterfaceImpl = transactionServiceInterfaceImpl;
        this.auditEventServiceInterfaceImpl = auditEventServiceInterfaceImpl;
    }

    /**
     * Start user interface
     */
    public void start() {

        frame.add(console);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        frame.setVisible(true);

        authorizationLoop(console);

        System.exit(0);
    }

    private void authorizationLoop(GUIConsoleInterface console) {
        String newLine = System.getProperty("line.separator");
        int eventId = 0;
        console.print("Enter login: ", Color.BLACK);
        try {
            String playersLogin = bufferInput.readLine();
            console.print("Enter password: ", Color.BLACK);
            String playersPassword = bufferInput.readLine();
            try {
                auditEventServiceInterfaceImpl.authorizeAdmin(playersLogin, playersPassword);
                Player playerToAuthorize = ApplicationContext.INSTANCE.get("player", Player.class);
                if (playerToAuthorize.getRole().equals(Role.ADMIN)) {
                    adminLoop(console, playerToAuthorize.getId(), eventId);
                } else {
                    userLoop(console, playerToAuthorize.getId(), eventId);
                }
            } catch (CredentialsErrorException | LoginNotFoundException exception) {
                console.print(exception.getMessage() + newLine, Color.RED);
                authorizationLoop(console);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void adminLoop(GUIConsoleInterface console, int playerId, int eventId) {
        String newLine = System.getProperty("line.separator");
        String viableInputParameters = "1. Player" + newLine +
                "2. Wallet" + newLine +
                "3. Transaction" + newLine +
                "4. Audit events" + newLine +
                "5. Switch to a user role" + newLine +
                "(type 'quit' to exit): ";

        console.print("Type entity to work with:" + newLine, Color.BLACK);
        console.print(viableInputParameters, Color.BLACK);

        String line;
        try {
            while ((line = bufferInput.readLine()) != null) {
                switch (line.toLowerCase()) {
                    case ("quit"):
                        bufferInput.close();
                    case ("player"):
                    case ("1"):
                        playerServiceLoop(console, playerId, eventId);
                    case ("wallet"):
                    case ("2"):
                        walletServiceLoop(console, playerId, eventId);
                    case ("transaction"):
                    case ("3"):
                        transactionServiceLoop(console, playerId, eventId);
                    case ("audit"):
                    case ("4"):
                        auditLoop(console, playerId, eventId);
                    case ("user"):
                    case ("5"):
                        userLoop(console, playerId, eventId);
                    case ("menu"):
                        console.print("Type entity to work with:" + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    case (";"):
                        adminLoop(console, playerId, eventId);
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

    private void userLoop(GUIConsoleInterface console, int playerId, int eventId) {
        String newLine = System.getProperty("line.separator");
        String viableInputParameters = "1. Check wallet balance" + newLine +
                "2. Create transaction" + newLine +
                "3. View transaction history" + newLine +
                "4. Switch to admin role (for admin only)" + newLine +
                "(type 'quit' to exit): ";

        console.print("Type command:" + newLine, Color.BLACK);
        console.print(viableInputParameters, Color.BLACK);

        int walletId = 0;
        try {
            walletId = walletServiceInterfaceImpl.findByPlayerId(playerId).getId();
        } catch (IdNotFoundException exception) {
            console.print(newLine + exception.getMessage() + newLine, Color.RED);
        }

        String line;
        try {
            while ((line = bufferInput.readLine()) != null) {
                switch (line.toLowerCase()) {
                    case ("quit"):
                        bufferInput.close();
                    case ("check"):
                    case ("1"):
                        String actionCheck = "Check wallet balance";
                        try {
                            console.print(walletServiceInterfaceImpl
                                    .findById(playerId) + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionCheck,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionCheck,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("create"):
                    case ("2"):
                        String actionCreate = "Create transaction";
                        console.print("Creating new transaction" + newLine, Color.BLACK);
                        console.print("Enter transaction id: ", Color.BLACK);
                        Integer idNumber = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter type DEBIT or CREDIT: ", Color.BLACK);
                        Type type = Type.valueOf(bufferInput.readLine().toUpperCase());
                        console.print("Enter value of the transaction: ", Color.BLACK);
                        BigDecimal value = new BigDecimal(Integer.parseInt(bufferInput.readLine()));
                        try {
                            transactionServiceInterfaceImpl.createTransaction
                                    (idNumber, walletId, type, value);
                            console.print("Transaction successfully created" + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionCreate,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdAlreadyExistsException | IdNotFoundException |
                                 InsufficientBalanceException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionCreate,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("view"):
                    case ("3"):
                        String actionViewTransactionHistory = "View transaction history";
                        try {
                            console.print(transactionServiceInterfaceImpl
                                    .findAllTransactionsByWalletId(walletId) +
                                    newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionViewTransactionHistory,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionViewTransactionHistory,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("admin"):
                    case ("4"):
                        if (playerServiceInterfaceImpl.findById(playerId).getRole().equals(Role.ADMIN)) {
                            adminLoop(console, playerId, eventId);
                        } else {
                            console.print("You don't have access to this command" + newLine, Color.RED);
                        }
                    case ("menu"):
                        console.print("Type entity to work with:" + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    case (";"):
                        userLoop(console, playerId, eventId);
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

    private void playerServiceLoop(GUIConsoleInterface console, int playerId, int eventId) {
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
                        String actionCreate = "Create player";
                        console.print("Creating new player" + newLine, Color.BLACK);
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
                            PlayerDTO playerDTOForPlayerCreation = new PlayerDTO
                                        (firstName, lastName, login, password, role);
                            playerServiceInterfaceImpl.createPlayer(playerDTOForPlayerCreation);
                            console.print("Player successfully created" + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionCreate,
                                        LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press any key to return to the menu ", Color.BLACK);
                        } catch (IdAlreadyExistsException | LoginAlreadyExistsException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionCreate,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find"):
                    case ("2"):
                        String actionFindById = "Find player by id";
                        console.print("Enter id: ", Color.BLACK);
                        try {
                            console.print(playerServiceInterfaceImpl
                                    .findById(Integer.parseInt(bufferInput.readLine())) + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFindById,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFindById,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find all"):
                    case ("3"):
                        String actionFindAll = "Find all players";
                        eventId++;
                        auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFindAll,
                                LocalDateTime.now(), ActionResult.SUCCESS);
                        console.print(playerServiceInterfaceImpl.findAllPlayers() + newLine, Color.BLACK);
                        console.print("Press ENTER to return to the menu ", Color.BLACK);
                        break;
                    case ("update"):
                    case ("4"):
                        String actionUpdate = "Update player";
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
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionUpdate,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Player successfully updated" + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionUpdate,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("delete"):
                    case ("5"):
                        String actionDelete = "Delete player";
                        console.print("Enter id to delete player: ");
                        try {
                            playerServiceInterfaceImpl.deletePlayer(Integer.parseInt(bufferInput.readLine()));
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionDelete,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Player successfully deleted" + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionDelete,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("menu"):
                        console.print("Type command: " + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    case ("back"):
                        adminLoop(console, playerId, eventId);
                    case (";"):
                        playerServiceLoop(console, playerId, eventId);
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

    private void walletServiceLoop(GUIConsoleInterface console, int playerId, int eventId) {
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
                        String actionCreate = "Create wallet";
                        console.print("Creating new wallet" + newLine, Color.BLACK);
                        console.print("Enter id: ", Color.BLACK);
                        Integer idNumber = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter id of the player who owns the wallet: ", Color.BLACK);
                        int playerIdForWalletCreation = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter initial wallet balance: ", Color.BLACK);
                        BigDecimal balance = new BigDecimal(Integer.parseInt(bufferInput.readLine()));
                        try {
                            walletServiceInterfaceImpl.createWallet
                                    (idNumber, playerIdForWalletCreation, balance);
                            console.print("Wallet successfully created" + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionCreate,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (PlayerIdAlreadyExistsException | IdAlreadyExistsException | IdNotFoundException
                                exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionCreate,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find"):
                    case ("2"):
                        String actionFind = "Find wallet by id";
                        console.print("Enter id: ", Color.BLACK);
                        try {
                            console.print(walletServiceInterfaceImpl
                                    .findById(Integer.parseInt(bufferInput.readLine())) + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFind,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFind,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find all"):
                    case ("3"):
                        String actionFindAll = "Find all wallets";
                        eventId++;
                        auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFindAll,
                                LocalDateTime.now(), ActionResult.SUCCESS);
                        console.print(walletServiceInterfaceImpl.findAllWallets() + newLine, Color.BLACK);
                        console.print("Press ENTER to return to the menu ", Color.BLACK);
                        break;
                    case ("update"):
                    case ("4"):
                        String actionUpdate = "Update wallet";
                        console.print("Updating wallet" + newLine, Color.BLACK);
                        console.print("Enter id of the wallet to be updated: ", Color.BLACK);
                        Integer idWalletToBeUpdated = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter new wallet balance: ", Color.BLACK);
                        BigDecimal newBalance = new BigDecimal(Integer.parseInt(bufferInput.readLine()));
                        try {
                            walletServiceInterfaceImpl.updateWallet(idWalletToBeUpdated, newBalance);
                            console.print("Wallet successfully updated" + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionUpdate,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionUpdate,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("delete"):
                    case ("5"):
                        String actionDelete = "Delete wallet";
                        console.print("Enter id to delete wallet: ");
                        try {
                            walletServiceInterfaceImpl.deleteWallet(Integer.parseInt(bufferInput.readLine()));
                            console.print("Wallet successfully deleted" + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionDelete,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionDelete,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("menu"):
                        console.print("Type command: " + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    case ("back"):
                        adminLoop(console, playerId, eventId);
                    case (";"):
                        walletServiceLoop(console, playerId, eventId);
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

    private void transactionServiceLoop(GUIConsoleInterface console, int playerId, int eventId) {
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
                        String actionCreate = "Create transaction";
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
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionCreate,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdAlreadyExistsException | IdNotFoundException |
                                 InsufficientBalanceException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionCreate,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find"):
                    case ("2"):
                        String actionFind = "Find transaction by id";
                        console.print("Enter id: ", Color.BLACK);
                        try {
                            console.print(transactionServiceInterfaceImpl
                                    .findById(Integer.parseInt(bufferInput.readLine())) + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFind,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFind,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find all"):
                    case ("3"):
                        String actionFindAll = "Find all transactions";
                        eventId++;
                        auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFindAll,
                                LocalDateTime.now(), ActionResult.SUCCESS);
                        console.print(transactionServiceInterfaceImpl.findAllTransactions() + newLine, Color.BLACK);
                        console.print("Press ENTER to return to the menu ", Color.BLACK);
                        break;
                    case ("find by wallet"):
                    case ("4"):
                        String actionFindByWalletId = "Find all transactions by wallet id";
                        console.print("Enter wallet id: ", Color.BLACK);
                        try {
                            console.print(transactionServiceInterfaceImpl
                                    .findAllTransactionsByWalletId(Integer.parseInt(bufferInput.readLine())) + 
                                    newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFindByWalletId,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionFindByWalletId,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("update"):
                    case ("5"):
                        String actionUpdate = "Update transaction";
                        console.print("Updating transaction" + newLine, Color.BLACK);
                        console.print("Enter id of the transaction to be updated: ", Color.BLACK);
                        Integer idTransactionToBeUpdated = Integer.parseInt(bufferInput.readLine());
                        console.print("Enter new transaction value: ", Color.BLACK);
                        BigDecimal newValue = new BigDecimal(Integer.parseInt(bufferInput.readLine()));
                        try {
                            transactionServiceInterfaceImpl.updateTransaction(idTransactionToBeUpdated, newValue);
                            console.print("Transaction successfully updated" + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionUpdate,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException | InsufficientBalanceException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionUpdate,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("delete"):
                    case ("6"):
                        String actionDelete = "Delete transaction";
                        console.print("Enter id to delete transaction: ");
                        try {
                            transactionServiceInterfaceImpl.deleteTransaction(Integer.parseInt(bufferInput.readLine()));
                            console.print("Transaction successfully deleted" + newLine, Color.BLACK);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionDelete,
                                    LocalDateTime.now(), ActionResult.SUCCESS);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            eventId++;
                            auditEventServiceInterfaceImpl.createEvent(eventId, playerId, actionDelete,
                                    LocalDateTime.now(), ActionResult.FAIL);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("menu"):
                        console.print("Type command: " + newLine, Color.BLACK);
                        console.print(viableInputParameters, Color.BLACK);
                        break;
                    case ("back"):
                        adminLoop(console, playerId, eventId);
                    case (";"):
                        transactionServiceLoop(console, playerId, eventId);
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

    private void auditLoop(GUIConsoleInterface console, int playerId, int eventId) {
        String newLine = System.getProperty("line.separator");
        String viableInputParameters = "1. Find event by id" + newLine +
                "2. Find all events" + newLine +
                "3. Find all events by player" + newLine +
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
                    case ("find"):
                    case ("1"):
                        console.print("Enter id: ", Color.BLACK);
                        try {
                            console.print(auditEventServiceInterfaceImpl
                                    .findById(Integer.parseInt(bufferInput.readLine())) + newLine, Color.BLACK);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        } catch (IdNotFoundException exception) {
                            console.print(exception.getMessage() + newLine, Color.RED);
                            console.print("Press ENTER to return to the menu ", Color.BLACK);
                        }
                        break;
                    case ("find all"):
                    case ("2"):
                        console.print(auditEventServiceInterfaceImpl.findAllEvents() + newLine, Color.BLACK);
                        console.print("Press ENTER to return to the menu ", Color.BLACK);
                        break;
                    case ("find all by player"):
                    case ("3"):
                        console.print("Enter player id: ", Color.BLACK);
                        try {
                            console.print(auditEventServiceInterfaceImpl
                                    .findAllEventsByPlayerId(Integer.parseInt(bufferInput.readLine())) +
                                    newLine, Color.BLACK);
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
                        adminLoop(console, playerId, eventId);
                    case (";"):
                        auditLoop(console, playerId, eventId);
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