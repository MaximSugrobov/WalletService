package ru.msugrobov.repositories;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.msugrobov.entities.*;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.exceptions.PlayerIdAlreadyExistsException;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class WalletRepositoryTest {

    private final List<Wallet> storage = new ArrayList<>();
    private final PlayerRepository testPlayerRepository = new PlayerRepository();
    private final WalletRepository testRepository = new WalletRepository();
    private Wallet initWallet;
    private final SoftAssertions walletRepositoryAssertion = new SoftAssertions();

    @BeforeEach
    public void initEach() {
        Player initPlayer = new Player("Maxim", "Sugrobov",
                "Max", "Pass", Role.ADMIN);
        this.testPlayerRepository.create(initPlayer);
        initPlayer.setId(this.testPlayerRepository.readByLogin("Max").getId());
        this.initWallet = new Wallet(initPlayer.getId(), new BigDecimal(10000));

        this.testRepository.create(initWallet);
        initWallet.setId(this.testRepository.readByPlayerId(initPlayer.getId()).getId());
    }

    @AfterEach
    public void cleanUpEach() {
        String DELETE_ALL_WALLETS = "DELETE FROM wallets WHERE player_id <> '1'";
        String DELETE_ALL_PLAYERS = "DELETE FROM players WHERE login <> 'admin'";
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statementClearWallets = connection.prepareStatement(DELETE_ALL_WALLETS);
            statementClearWallets.executeUpdate();
            PreparedStatement statementClearPlayers = connection.prepareStatement(DELETE_ALL_PLAYERS);
            statementClearPlayers.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test for creating new wallet")
    public void createWalletTest(){
        Player playerForWalletCreation = new Player("firstName", "lastName",
                "Login", "Password", Role.USER);
        this.testPlayerRepository.create(playerForWalletCreation);
        Wallet wallet = new Wallet(this.testPlayerRepository.readByLogin("Login").getId(), new BigDecimal(10000));

        this.testRepository.create(wallet);

        walletRepositoryAssertion.assertThat(this.testRepository).isNotNull();
        walletRepositoryAssertion.assertThat(this.testRepository.readAll()).hasSize(3);
        walletRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for creating new wallet with existing playerId")
    public void createWalletWithExistingPlayerIdTest(){
        Wallet walletWithExistingPlayerId = new Wallet(1, new BigDecimal(10000));

        walletRepositoryAssertion.assertThatThrownBy(() -> this.testRepository.create(walletWithExistingPlayerId))
                .isInstanceOf(PlayerIdAlreadyExistsException.class).hasMessageContaining("playerId");
        walletRepositoryAssertion.assertThat(this.testRepository.readAll()).hasSize(2);
        walletRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for reading wallet info by id")
    public void readTest() {
        Wallet newWallet = this.testRepository.readById(initWallet.getId());
        assertThat(newWallet).usingRecursiveComparison().isEqualTo(this.initWallet);
    }

    @Test
    @DisplayName("Test for reading wallet info by not existing id")
    public void readByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.readById(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading wallet info by player id")
    public void readByPlayerIdTest() {
        Wallet newWallet = this.testRepository.readByPlayerId(initWallet.getPlayerId());
        assertThat(newWallet).usingRecursiveComparison().isEqualTo(this.initWallet);
    }

    @Test
    @DisplayName("Test for reading wallet info by not existing player id")
    public void readByNotExistingPlayerIdTest() {
        assertThatThrownBy(() -> this.testRepository.readByPlayerId(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("playerId");
    }

    @Test
    @DisplayName("Test for updating wallet")
    public void updateTest() {
        Wallet updatedWallet = new Wallet(initWallet.getId(), initWallet.getPlayerId(), new BigDecimal(20000));
        this.testRepository.update(initWallet.getId(), updatedWallet);
        assertThat(this.testRepository.readById(initWallet.getId()))
                .usingRecursiveComparison().isEqualTo(updatedWallet);
    }

    @Test
    @DisplayName("Test for updating wallet with wrong parameters")
    public void updateWithWrongParametersTest() {
        Wallet updatedWallet = new Wallet(2, 2, new BigDecimal(20000));
        this.testRepository.update(initWallet.getId(), updatedWallet);
        assertThat(this.testRepository.readById(initWallet.getId()))
                .usingRecursiveComparison().isNotEqualTo(updatedWallet);
    }

    @Test
    @DisplayName("Test for updating wallet by not existing id")
    public void updateByNotExistingIdTest() {
        Wallet updatedWallet = new Wallet(1, 1, new BigDecimal(20000));
        assertThatThrownBy(() -> this.testRepository.update(2, updatedWallet))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for deleting wallet")
    public void deleteTest() {
        this.testRepository.delete(initWallet.getId());
        assertThat(this.testRepository.readAll()).hasSize(1);
    }

    @Test
    @DisplayName("Test for deleting wallet by not existing id")
    public void deleteByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.delete(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading all wallets from storage")
    public void readAllTest() {
        Wallet adminWallet = new Wallet(1, 1, new BigDecimal(10000));
        this.storage.add(adminWallet);
        this.storage.add(initWallet);

        List<Wallet> allWalletsInStorage = this.testRepository.readAll();

        walletRepositoryAssertion.assertThat(allWalletsInStorage)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        walletRepositoryAssertion.assertThat(allWalletsInStorage).hasSize(2);
        walletRepositoryAssertion.assertAll();
    }
}
