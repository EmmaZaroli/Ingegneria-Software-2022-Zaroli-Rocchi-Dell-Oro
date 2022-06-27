package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.dtos.CharacterCardDto;
import it.polimi.ingsw.dtos.IslandCardDto;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.CharacterCardWithSetUpAction;
import it.polimi.ingsw.model.ExpertPlayer;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.utils.ApplicationConstants;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CharacterCardHelper {

    //check server side
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

    //check server side
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

    //check client and server side
    public static boolean areParametersOkCharacter9(Object[] parameters) {
        return (parameters.length == 1) && (parameters[0] instanceof PawnColor);
    }

    //check server side
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
        if(player.getBoard().getStudentsInDiningRoom((PawnColor) parameters[0]) >= ApplicationConstants.STUDENTS_IN_DININGROOM)
            return false;
        return true;
    }
}
