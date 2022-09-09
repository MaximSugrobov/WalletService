package ru.msugrobov;

import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.impl.PlayerServiceInterfaceImpl;
import ru.msugrobov.services.impl.UserInterfaceServiceImpl;

import java.util.ArrayList;

public class WalletService {

    public static void main(String[] args) {
        UserInterfaceServiceImpl userInterfaceService = new UserInterfaceServiceImpl(
                new PlayerServiceInterfaceImpl(new PlayerRepository(new ArrayList<>())));
        userInterfaceService.start();
    }
}