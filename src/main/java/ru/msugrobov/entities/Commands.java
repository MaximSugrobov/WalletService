package ru.msugrobov.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Commands {
    private int id;
    private int playerId;
    private String action;
    private Date date;
}
