package ru.msugrobov;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter first and last name of the player: ");
        String playerName = in.nextLine();

        System.out.println("Enter players balance: ");
        double balance = in.nextDouble();

        Map<String, Double> balanceByPlayer = new HashMap<>();
        balanceByPlayer.put(playerName, balance);
        System.out.println(balanceByPlayer);

    }
}