package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

import java.util.ArrayList;
import java.util.List;

public class ProfessorTable {
    private boolean yellow;
    private boolean blue;
    private boolean green;
    private boolean red;
    private boolean pink;
    
    public ProfessorTable() {
        this.yellow = false;
        this.blue = false;
        this.green = false;
        this.red = false;
        this.pink = false;
    }

    public ProfessorTable(boolean yellow, boolean blue, boolean green, boolean red, boolean pink) {
        this.yellow = yellow;
        this.blue = blue;
        this.green = green;
        this.red = red;
        this.pink = pink;
    }

    public boolean isThereProfessor(PawnColor color) {
        return switch (color) {
            case YELLOW -> yellow;
            case BLUE -> blue;
            case GREEN -> green;
            case RED -> red;
            case PINK -> pink;
        };
    }

    private void changeProfessorStatus(PawnColor color, boolean status) {
        switch (color) {
            case YELLOW -> yellow = status;
            case BLUE -> blue = status;
            case GREEN -> green = status;
            case RED -> red = status;
            case PINK -> pink = status;
        }
    }

    public void addProfessor(PawnColor color) {
        changeProfessorStatus(color, true);
    }

    public void removeProfessor(PawnColor color) {
        changeProfessorStatus(color, false);
        //TODO se provo a rimuovere un professore non presente?
    }

    //TODO can it be reduce?
    public List<PawnColor> getProfessors(){
        List listProfessors = new ArrayList();
        if(this.blue) listProfessors.add(PawnColor.BLUE);
        if(this.red) listProfessors.add(PawnColor.RED);
        if(this.yellow) listProfessors.add(PawnColor.YELLOW);
        if(this.green) listProfessors.add(PawnColor.GREEN);
        if(this.pink) listProfessors.add(PawnColor.PINK);
        return listProfessors;
    }
}
