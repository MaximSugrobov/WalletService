package ru.msugrobov;

import ru.msugrobov.entities.*;
import ru.msugrobov.repositories.AuditEventRepository;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.repositories.TransactionRepository;
import ru.msugrobov.repositories.WalletRepository;
import ru.msugrobov.services.impl.*;

public class WalletService {

    public static void main(String[] args) {
        initUIS();
    }

    public static void initUIS() {

        Player admin = new Player(1, "Maxim", "Sug", "admin", "admin", Role.ADMIN);
        Player user = new Player(2, "User", "User", "user", "user", Role.USER);
        PlayerRepository playerRepository = new PlayerRepository();
        playerRepository.create(admin);
        playerRepository.create(user);
        PlayerServiceInterfaceImpl playerServiceInterfaceImpl = new PlayerServiceInterfaceImpl(playerRepository);

        WalletRepository walletRepository = new WalletRepository();
        WalletServiceInterfaceImpl walletServiceInterfaceImpl = new WalletServiceInterfaceImpl(walletRepository);

        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionServiceInterfaceImpl transactionServiceInterfaceImpl = new TransactionServiceInterfaceImpl
                (transactionRepository);

        AuditEventRepository auditEventRepository = new AuditEventRepository();
        AuditEventServiceInterfaceImpl auditEventServiceInterfaceImpl = new AuditEventServiceInterfaceImpl
                (auditEventRepository);

        UserInterfaceServiceImpl userInterfaceService = new UserInterfaceServiceImpl
                (playerServiceInterfaceImpl, walletServiceInterfaceImpl,
                        transactionServiceInterfaceImpl, auditEventServiceInterfaceImpl);
        userInterfaceService.start();
    }
}