package ru.msugrobov.services.impl;

import bsh.util.JConsole;
import ru.msugrobov.entities.Player;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.PlayerServiceInterface;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;

public class PlayerServiceInterfaceImpl implements PlayerServiceInterface {

    private final PlayerRepository playerRepository;

    public PlayerServiceInterfaceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAllPlayers() {
        return this.playerRepository.readAll();
    }

    public Player findById(int idNumber) {
        return this.playerRepository.readById(idNumber);
    }

    public void createPlayer() {

    }

    public void updatePlayer() {

    }

    public void deletePlayer() {

    }
}
