package it.polimi.ingsw.applications;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.client.gui.Gui;

public class Client {

    private View view;

    public static void main(String[] args) {
        if (args.length == 0) {
            Gui.main(args);
        } else {
            if (args[0].equals("--cli")) {
                new Client(new Cli());
            } else {
                System.out.println("Unrecognized command\n Enter no arguments to start the GUI or --cli to start the CLI");
            }
        }
    }

    public Client(View view) {
        this.view = view;
        view.start();
    }
}
