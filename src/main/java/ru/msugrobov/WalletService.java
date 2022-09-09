package ru.msugrobov;

import ru.msugrobov.services.impl.UserInterfaceServiceImpl;

public class WalletService {

    public static void main(String[] args) {
        UserInterfaceServiceImpl userInterfaceService = new UserInterfaceServiceImpl();
        userInterfaceService.start();
    }
}