package ru.msugrobov.services.impl;

import ru.msugrobov.entities.Player;
import ru.msugrobov.entities.Role;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.PlayerServiceInterface;

import java.util.List;

public class PlayerServiceInterfaceImpl implements PlayerServiceInterface {

    private final PlayerRepository playerRepository;

    public PlayerServiceInterfaceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;Player player = new Player(1, "Maxim",
                "Sugrobov", "Max", "pass", Role.ADMIN);
        this.playerRepository.create(player);

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

    public void deletePlayer(int idNumber) {
        this.playerRepository.delete(idNumber);
    }
}
