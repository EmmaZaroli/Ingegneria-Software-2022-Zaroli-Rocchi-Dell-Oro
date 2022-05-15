package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.exceptions.NoCoinsAvailableException;
import it.polimi.ingsw.model.ExpertTable;

public class ExpertTableController extends TableController {
    public ExpertTableController(ExpertTable table) {
        super(table);
    }

    public void takeCoin() throws NoCoinsAvailableException {
        if (((ExpertTable) table).getCoins() == 0) {
            throw new NoCoinsAvailableException();
        }
        ((ExpertTable) table).takeCoin();
    }

    public void depositCoins(int coins) {
        ((ExpertTable) table).depositCoins(coins);
    }
}
