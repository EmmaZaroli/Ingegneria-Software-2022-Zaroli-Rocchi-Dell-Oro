package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.ExpertTable;

public class ExpertTableController extends TableController {
    public ExpertTableController(ExpertTable table) {
        super(table);
    }

    public void takeCoin() {
        //TODO check if coins > 0
        ((ExpertTable) table).takeCoin();
    }

    public void depositCoins(int coins) {
        ((ExpertTable) table).depositCoins(coins);
    }
}
