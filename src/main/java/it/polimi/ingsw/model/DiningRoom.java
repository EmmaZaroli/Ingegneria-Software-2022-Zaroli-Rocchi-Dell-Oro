package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

public class DiningRoom {
    private int yellow;
    private int blue;
    private int green;
    private int red;
    private int pink;

    public DiningRoom() {
        this.yellow = 0;
        this.blue = 0;
        this.green = 0;
        this.red = 0;
        this.pink = 0;
    }

    public DiningRoom(int yellow, int blue, int green, int red, int pink) {
        this.yellow = yellow;
        this.blue = blue;
        this.green = green;
        this.red = red;
        this.pink = pink;
    }

    public int getStudents(PawnColor color) {
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
        return 0;
        //TODO gestire il caso di default con un Optional?
    }

    public void addStudent(PawnColor color) {
        switch (color) {
            case YELLOW:
                yellow++;
                break;
            case BLUE:
                blue++;
                break;
            case GREEN:
                green++;
                break;
            case RED:
                red++;
                break;
            case PINK:
                pink++;
                break;
        }
        //TODO gestione nel caso si superi il limite di studenti
        //TODO riutilizzare il codice di addStudents() ?
    }

    public void addStudents(PawnColor color, int n) {
        switch (color) {
            case YELLOW:
                yellow += n;
                break;
            case BLUE:
                blue += n;
                break;
            case GREEN:
                green += n;
                break;
            case RED:
                red += n;
                break;
            case PINK:
                pink += n;
                break;
        }
        //TODO gestione nel caso si superi il limite di studenti
    }
}