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
        return switch (color){
            case YELLOW -> yellow;
            case BLUE -> blue;
            case GREEN -> green;
            case RED -> red;
            case PINK -> pink;
        };
    }

    public void addStudents(PawnColor color, int n) {
        switch (color) {
            case YELLOW -> yellow += n;
            case BLUE -> blue += n;
            case GREEN -> green += n;
            case RED -> red += n;
            case PINK -> pink += n;
        }
        //TODO gestione nel caso si superi il limite di studenti
    }

    public void addStudent(PawnColor color) {
        addStudents(color, 1);
    }
}