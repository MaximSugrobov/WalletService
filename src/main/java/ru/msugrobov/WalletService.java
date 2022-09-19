package ru.msugrobov;

import ru.msugrobov.entities.Player;
import ru.msugrobov.entities.Transaction;
import ru.msugrobov.entities.Wallet;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.repositories.TransactionRepository;
import ru.msugrobov.repositories.WalletRepository;
import ru.msugrobov.services.impl.PlayerServiceInterfaceImpl;
import ru.msugrobov.services.impl.TransactionServiceInterfaceImpl;
import ru.msugrobov.services.impl.UserInterfaceServiceImpl;
import ru.msugrobov.services.impl.WalletServiceInterfaceImpl;

import java.util.ArrayList;
import java.util.List;

public class WalletService {

    public static void main(String[] args) {
        initUIS();
    }

    public static void initUIS() {

        List<Player> player = new ArrayList<>();
        PlayerRepository playerRepository = new PlayerRepository(player);
        PlayerServiceInterfaceImpl playerServiceInterfaceImpl = new PlayerServiceInterfaceImpl(playerRepository);

        List<Wallet> wallet = new ArrayList<>();
        WalletRepository walletRepository = new WalletRepository(wallet);
        WalletServiceInterfaceImpl walletServiceInterfaceImpl = new WalletServiceInterfaceImpl(walletRepository);

        List<Transaction> transaction = new ArrayList<>();
        TransactionRepository transactionRepository = new TransactionRepository(transaction);
        TransactionServiceInterfaceImpl transactionServiceInterfaceImpl = new TransactionServiceInterfaceImpl
                (transactionRepository);

        UserInterfaceServiceImpl userInterfaceService = new UserInterfaceServiceImpl
                (playerServiceInterfaceImpl, walletServiceInterfaceImpl, transactionServiceInterfaceImpl);
        userInterfaceService.start();
    }
}