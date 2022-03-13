package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.*;
import java.util.*;

public class Table {
    private final ArrayList<PawnColor> professors;
    private final List<IslandCard> islandCards;
    private final Bag bag;
    private final List<CloudTile> cloudTiles;

    public Table(PlayerCountIcon playerCountIcon) {
        professors = new ArrayList<>();
        professors.addAll(Arrays.asList(PawnColor.values()));
        islandCards = new ArrayList<>();
        for(int i=0; i<12; i++){
            this.islandCards.add(new IslandCard());
        }
        this.bag = new Bag();
        cloudTiles = new ArrayList<>();
        this.cloudTiles.add(new CloudTile(playerCountIcon));
        this.cloudTiles.add(new CloudTile(playerCountIcon));
        if(playerCountIcon.equals(PlayerCountIcon.THREE)){
            this.cloudTiles.add(new CloudTile(playerCountIcon));
        }
    }

    public void fillClouds(){
        for(CloudTile cloud : cloudTiles) {
            // List<PawnColor> studentsDrawn = bag.drawStudents();
            //cloud.AddStudents(studentsDrawn);
        }
    }

    //check if the professor is still available on the table
    private boolean ProfessorAvailable(PawnColor professor) {
        return professors.contains(professor);
    }

    //remove professor from the List
    public void removeProfessor(PawnColor professor) {
        professors.remove(professor);
    }

    //take professor
    public boolean takeProfessor(PawnColor professor) {
        if(this.ProfessorAvailable(professor))
        {
            this.removeProfessor(professor);
            return true;
        }
        return false;
    }

    public List<PawnColor> takeStudentsFromCloud(CloudTile cloud){
        List<PawnColor> students = cloud.takeStudentsFromCloud();
        cloud.clearCloud();
        return students;
    }

    public void moveMotherNature(int move){}
    public void movePawnOnIsland(PawnColor student,IslandCard Island){}

    /*
    public int countInfluenceOnIsland(List<PawnColor> Playerprofessors){
        for(IslandCard island : islandCards){
            if(island.isHasMotherNature()){
                int count = island.countInfluence(Playerprofessors);
                return count;
            }
        }
    }
     */
}
