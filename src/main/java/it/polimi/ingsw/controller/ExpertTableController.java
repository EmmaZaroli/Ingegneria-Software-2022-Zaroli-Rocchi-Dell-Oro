package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.PlayerCountIcon;

public class ExpertTableController extends TableController {
    private int coins;

    public ExpertTableController(PlayerCountIcon playerCountIcon) {
        super(playerCountIcon);
        coins = playerCountIcon.equals(PlayerCountIcon.TWO_FOUR) ? 18 : 17;
    }

    public void PickCoin() {
        //TODO check if coins > 0
        coins--;
    }

    public void DepositCoins(int coins) {
        this.coins += coins;
    }
}
