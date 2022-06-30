package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.dtos.CharacterCardDto;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.CharacterCardWithSetUpAction;
import it.polimi.ingsw.model.ExpertPlayer;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.utils.ApplicationConstants;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Character Card Helper
 * Classed used server-side for checking the validity of the parameters of each characterCard played by the player
 */
public class CharacterCardHelper {

    /**
     * Checking if the parameters of character_1 are valid
     * @param messageCard the CharacterCardDto
     * @param parameters  the parameters
     * @param islands the islandCards
     * @return true if the parameters are valid, false otherwise
     */
    public static boolean areParametersOkCharacter1(CharacterCardDto messageCard, CharacterCard serverCard, Object[] parameters, List<IslandCard> islands){
        if(parameters.length != 2)
            return false;
        if (!(parameters[0] instanceof PawnColor && parameters[1] instanceof UUID))
            return false;
        if(!(messageCard.isWithSetUpAction()))
            return false;
        if(!(messageCard.getStudents().contains(parameters[0])))
            return false;
        if(!(serverCard instanceof CharacterCardWithSetUpAction))
            return false;
        if(!(((CharacterCardWithSetUpAction)serverCard).getStudents().contains(parameters[0])))
            return false;
        for (IslandCard island : islands) {
            if (island.getUuid().equals(parameters[1]))
                return true;
        }
        return false;
    }

    /**
     * Checking if the parameters of character_7 are valid
     * @param messageCard the CharacterCardDto
     * @param parameters the parameters
     * @param serverCard the characterCard
     * @param player the expertPlayer
     * @return true if the parameters are valid, false otherwise
     */
    public static boolean areParametersOkCharacter7(CharacterCardDto messageCard, CharacterCard serverCard, Object[] parameters, ExpertPlayer player){
        if(parameters.length != 2)
            return false;
        if (!(parameters[0] instanceof List<?> && parameters[1] instanceof List<?>))
            return false;
        if(!(messageCard.isWithSetUpAction()))
            return false;
        if(!(serverCard instanceof CharacterCardWithSetUpAction))
            return false;
        List<PawnColor> colorsFromCard = (List<PawnColor>) parameters[0];
        List<PawnColor> colorsFromEntrance = (List<PawnColor>) parameters[1];
        Map<PawnColor, Integer> cardinalityCard = ((CharacterCardWithSetUpAction)serverCard).getStudentsCardinality();
        Map<PawnColor, Integer> cardinalityEntrance = player.getBoard().getStudentsInEntranceCardinality();
        for (PawnColor color : PawnColor.values()) {
            if (colorsFromCard.stream().filter(x -> x == color).count() > cardinalityCard.get(color))
                return false;
        }
        for (PawnColor color : PawnColor.values()) {
            if (colorsFromEntrance.stream().filter(x -> x == color).count() > cardinalityEntrance.get(color))
                return false;
        }
        return true;
    }

    /**
     * Checking if the parameters of character_9 are valid
     * @param parameters the parameters
     * @return true if the parameters are valid, false otherwise
     */
    public static boolean areParametersOkCharacter9(Object[] parameters) {
        return (parameters.length == 1) && (parameters[0] instanceof PawnColor);
    }

    /**
     * Checking if the parameters of character_11 are valid
     * @param messageCard the characterCardDto
     * @param serverCard the characterCard
     * @param parameters the parameters
     * @param player the expertPlayer
     * @return true if the parameters are valid, false otherwise
     */
    public static boolean areParametersOkCharacter11(CharacterCardDto messageCard, CharacterCard serverCard, Object[] parameters, ExpertPlayer player){
        if(parameters.length != 1)
            return false;
        if (!(parameters[0] instanceof PawnColor))
            return false;
        if(!(messageCard.isWithSetUpAction()))
            return false;
        if(!(serverCard instanceof CharacterCardWithSetUpAction))
            return false;
        if(!((CharacterCardWithSetUpAction)serverCard).getStudents().contains(parameters[0]))
            return false;
        return player.getBoard().getStudentsInDiningRoom((PawnColor) parameters[0]) < ApplicationConstants.STUDENTS_IN_DININGROOM;
    }
}
