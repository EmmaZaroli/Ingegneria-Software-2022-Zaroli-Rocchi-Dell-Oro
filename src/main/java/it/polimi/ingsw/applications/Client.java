package it.polimi.ingsw.applications;

import it.polimi.ingsw.client.View;

public class Client {

    private View view;

    public static void main(String[] args) {
        if (args.length == 0) {
            //TODO start gui
        } else {
            if (args[0].equals("--cli")) {
                //TODO start cli
            }
        }
    }

    public Client(View view) {
        this.view = view;
    }
}
