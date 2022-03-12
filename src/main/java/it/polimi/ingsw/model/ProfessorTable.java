package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

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
        switch (color) {
            case YELLOW:
                return yellow;
            case BLUE:
                return blue;
            case GREEN:
                return green;
            case RED:
                return red;
            case PINK:
                return pink;
        }
        return false;
        //TODO gestire il possibile caso di default con un Optional?
    }

    public void addProfessor(PawnColor color) {
        switch (color) {
            case YELLOW:
                yellow = true;
                break;
            case BLUE:
                blue = true;
                break;
            case GREEN:
                green = true;
                break;
            case RED:
                red = true;
                break;
            case PINK:
                pink = true;
                break;
        }
    }

    public void removeProfessor(PawnColor color) {
        switch (color) {
            case YELLOW:
                yellow = false;
                break;
            case BLUE:
                blue = false;
                break;
            case GREEN:
                green = false;
                break;
            case RED:
                red = false;
                break;
            case PINK:
                pink = false;
                break;
        }
        //TODO se provo a rimuovere un professore non presente?
    }
}
