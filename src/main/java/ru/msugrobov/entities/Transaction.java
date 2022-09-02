package ru.msugrobov.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    private int id;
    private int walletID;
    private Type type;
    private BigDecimal value;
}
