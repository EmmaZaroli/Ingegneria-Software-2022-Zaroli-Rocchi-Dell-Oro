package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.exceptions.NoCoinsAvailableException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.HashSet;
import java.util.Set;

/**
 * ExpertTableController
 */
public class ExpertTableController extends TableController {
    public ExpertTableController(ExpertTable table, ExpertGameParameters parameters) {
        super(table, parameters);
    }

    /**
     *
     * @throws NoCoinsAvailableException if there aren't coins on the table
     */
    public void takeCoin() throws NoCoinsAvailableException {
        if (((ExpertTable) table).getCoins() == 0) {
            throw new NoCoinsAvailableException();
        }
        ((ExpertTable) table).takeCoin();
    }

    /**
     *
     * @param coins the coins to adds on the table
     */
    public void depositCoins(int coins) {
        ((ExpertTable) table).depositCoins(coins);
    }

    @Override
    public int countInfluenceOnIsland(Set<PawnColor> playerProfessors, Tower towerColor) {
        Set<PawnColor> effectivePlayerProfessors = new HashSet<>();
        effectivePlayerProfessors.addAll(playerProfessors);
        effectivePlayerProfessors.remove(getParameters().getColorWithNoInfluence());

        if(getParameters().isTowersCountInInfluence() || table.getIslands().get(table.getIslandWithMotherNature()).getTower() != towerColor){
            return getParameters().getExtraInfluence() + table.getIslands().get(table.getIslandWithMotherNature()).countInfluence(effectivePlayerProfessors, towerColor);
        }
        else
        {
            return getParameters().getExtraInfluence() + table.getIslands().get(table.getIslandWithMotherNature()).countInfluence(effectivePlayerProfessors, towerColor) - table.getIslands().get(table.getIslandWithMotherNature()).getSize();
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
