package ru.msugrobov;

import ru.msugrobov.entities.*;
import ru.msugrobov.repositories.AuditEventRepository;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.repositories.TransactionRepository;
import ru.msugrobov.repositories.WalletRepository;
import ru.msugrobov.services.impl.*;

import java.util.ArrayList;
import java.util.List;

public class WalletService {

    public static void main(String[] args) {
        initUIS();
    }

    public static void initUIS() {

        List<Player> playerStorage = new ArrayList<>();
        Player admin = new Player(1, "Maxim", "Sug", "admin", "admin", Role.ADMIN);
        Player user = new Player(2, "User", "User", "user", "user", Role.USER);
        playerStorage.add(admin);
        playerStorage.add(user);
        PlayerRepository playerRepository = new PlayerRepository(playerStorage);
        PlayerServiceInterfaceImpl playerServiceInterfaceImpl = new PlayerServiceInterfaceImpl(playerRepository);

        List<Wallet> walletStorage = new ArrayList<>();
        WalletRepository walletRepository = new WalletRepository(walletStorage);
        WalletServiceInterfaceImpl walletServiceInterfaceImpl = new WalletServiceInterfaceImpl(walletRepository);

        List<Transaction> transactionStorage = new ArrayList<>();
        TransactionRepository transactionRepository = new TransactionRepository(transactionStorage);
        TransactionServiceInterfaceImpl transactionServiceInterfaceImpl = new TransactionServiceInterfaceImpl
                (transactionRepository);

        List<AuditEvent> auditEventStorage = new ArrayList<>();
        AuditEventRepository auditEventRepository = new AuditEventRepository(auditEventStorage);
        AuditEventServiceInterfaceImpl auditEventServiceInterfaceImpl = new AuditEventServiceInterfaceImpl
                (auditEventRepository);

        UserInterfaceServiceImpl userInterfaceService = new UserInterfaceServiceImpl
                (playerServiceInterfaceImpl, walletServiceInterfaceImpl,
                        transactionServiceInterfaceImpl, auditEventServiceInterfaceImpl);
        userInterfaceService.start();
    }
}