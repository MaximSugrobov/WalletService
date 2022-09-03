package ru.msugrobov.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    private Integer id;
    private int walletId;
    private Type type;
    private BigDecimal value;
}
