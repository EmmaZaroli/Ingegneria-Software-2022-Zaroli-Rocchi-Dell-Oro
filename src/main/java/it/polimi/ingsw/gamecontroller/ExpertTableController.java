package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.exceptions.NoCoinsAvailableException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.Set;

public class ExpertTableController extends TableController {
    public ExpertTableController(ExpertTable table, ExpertGameParameters parameters) {
        super(table, parameters);
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

    @Override
    public int countInfluenceOnIsland(Set<PawnColor> playerProfessors, Tower towerColor) {
        if(getParameters().isTowersCountInInfluence() || table.getIslands().get(table.getIslandWithMotherNature()).getTower() != towerColor){
            return table.getIslands().get(table.getIslandWithMotherNature()).countInfluence(playerProfessors, towerColor);
        }
        else
        {
            return table.getIslands().get(table.getIslandWithMotherNature()).countInfluence(playerProfessors, towerColor) - table.getIslands().get(table.getIslandWithMotherNature()).getSize();
        }

    }

    @Override
    public ExpertTable getTable() {
        return (ExpertTable)super.getTable();
    }

    @Override
    public ExpertGameParameters getParameters() {
        return (ExpertGameParameters)super.getParameters();
    }
}
