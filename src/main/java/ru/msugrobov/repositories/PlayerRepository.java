package ru.msugrobov.repositories;

import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.entities.Player;
import ru.msugrobov.exceptions.LoginNotFoundException;

import java.util.List;
import java.util.Objects;

public class PlayerRepository implements RepositoryInterface<Player> {

    private final List<Player> storage;

    public PlayerRepository(List<Player> storage) {
        this.storage = storage;
    }

    private boolean existByLogin(String login) {
        return storage.stream().anyMatch(currentRecord -> Objects.equals(currentRecord.getLogin(), login));
    }

    private boolean existById(int idNumber) {
        return storage.stream().anyMatch(currentRecord -> Objects.equals(currentRecord.getId(), idNumber));
    }

    public Player readById(int idNumber) {
        return storage.stream()
                .filter(currentRecord -> currentRecord.getId() == idNumber)
                .findAny()
                .orElseThrow(() -> new IdNotFoundException("Player with given id does not exists"));
    }

    public void create(Player player) {
        if (!existByLogin(player.getLogin()) && !existById(player.getId())) {
            storage.add(player);
        } else if (existByLogin(player.getLogin())) {
            throw new LoginNotFoundException("Player with given login already exists");
        } else if (existById(player.getId())) {
            throw new IdNotFoundException("Player with given id does not exists");
        }
    }

    public void update(int idNumber, Player player) {
        Player findPlayerById = storage.stream()
                .filter(currentRecord -> currentRecord.getId() == idNumber)
                .findAny()
                .orElseThrow(() -> new IdNotFoundException("Player with given id does not exists"));
        findPlayerById.setFirstName(player.getFirstName());
        findPlayerById.setLastName(player.getLastName());
        findPlayerById.setPassword(player.getPassword());
        System.out.print("Player was updated successfully");
    }

    public void delete(int idNumber) {
        Player findPlayerById = storage.stream()
                .filter(currentRecord -> currentRecord.getId() == idNumber)
                .findAny()
                .orElseThrow(() -> new IdNotFoundException("Player with given id does not exists"));
        storage.remove(findPlayerById);
    }
}
