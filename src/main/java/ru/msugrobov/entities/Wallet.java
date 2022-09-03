package ru.msugrobov.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Wallet {
    private Integer id;
    private int playerId;
    private BigDecimal balance;
}
