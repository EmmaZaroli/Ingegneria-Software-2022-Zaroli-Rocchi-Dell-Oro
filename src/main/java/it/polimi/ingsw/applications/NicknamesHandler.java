package it.polimi.ingsw.applications;

public class NicknamesHandler {

    private NicknamesHandler() {
    }

    private static NicknamesHandler instance;

    public static NicknamesHandler getInstance() {
        if (instance == null) {
            instance = new NicknamesHandler();
        }
        return instance;
    }

    //TODO move here the code to handle the nicknames and remove Server's references around the code
}
