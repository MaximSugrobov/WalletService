package ru.msugrobov.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.msugrobov.entities.Player;
import ru.msugrobov.entities.Wallet;
import ru.msugrobov.entities.Role;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WalletRepositoryTest {

    private final List<Wallet> storage = new ArrayList<>();

    private final WalletRepository testRepository = new WalletRepository(storage);

    private Wallet initWallet;

    @BeforeEach
    public void initEach() {
        Player initPlayer = new Player(1, "Maxim", "Sugrobov",
                "Max", "Pass", Role.ADMIN);
        this.initWallet = new Wallet(1, initPlayer.getId(), new BigDecimal(10000));
        this.testRepository.create(initWallet);
    }

    @AfterEach
    public void cleanUpEach() {
        this.storage.clear();
    }

    @Test
    @DisplayName("Test for creating new wallet")
    public void createWalletTest(){
        Wallet wallet = new Wallet(2, 2, new BigDecimal(10000));
        this.testRepository.create(wallet);
    }

    @Test
    @DisplayName("Test for creating new wallet with existing id")
    public void createWalletWithExistingIdTest(){
        Wallet walletWithExistingId = new Wallet(1, 2, new BigDecimal(10000));
        assertThatThrownBy(() -> this.testRepository.create(walletWithExistingId))
                .isInstanceOf(IdAlreadyExistsException.class).hasMessageContaining("id");
        assertThat(storage).hasSize(1);
    }

    @Test
    @DisplayName("Test for creating new wallet with existing playerId")
    public void createWalletWithExistingPlayerIdTest(){
        Wallet walletWithExistingPlayerId = new Wallet(2, 1, new BigDecimal(10000));
        assertThatThrownBy(() -> this.testRepository.create(walletWithExistingPlayerId))
                .isInstanceOf(IdAlreadyExistsException.class).hasMessageContaining("playerId");
        assertThat(storage).hasSize(1);
    }

    @Test
    @DisplayName("Test for reading wallet info by id")
    public void readTest() {
        Wallet newWallet = this.testRepository.readById(1);
        assertThat(newWallet).usingRecursiveComparison().isEqualTo(this.initWallet);
    }

    @Test
    @DisplayName("Test for reading wallet info by not existing id")
    public void readByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.readById(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for updating wallet")
    public void updateTest() {
        Wallet updatedWallet = new Wallet(1, 1, new BigDecimal(20000));
        this.testRepository.update(1, updatedWallet);
        assertThat(this.initWallet).usingRecursiveComparison().isEqualTo(updatedWallet);
    }

    @Test
    @DisplayName("Test for updating wallet with wrong parameters")
    public void updateWithWrongParametersTest() {
        Wallet updatedWallet = new Wallet(2, 2, new BigDecimal(20000));
        this.testRepository.update(1, updatedWallet);
        assertThat(this.initWallet).usingRecursiveComparison().isNotEqualTo(updatedWallet);
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
        this.testRepository.delete(1);
        assertThat(this.storage).hasSize(0);
    }

    @Test
    @DisplayName("Test for deleting wallet by not existing id")
    public void deleteByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.delete(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }
}
