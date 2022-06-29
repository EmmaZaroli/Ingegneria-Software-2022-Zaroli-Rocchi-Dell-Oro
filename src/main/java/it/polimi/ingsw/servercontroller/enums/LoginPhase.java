package it.polimi.ingsw.servercontroller.enums;

/**
 * Constants used by the userHandler during the login phase
 */
public enum LoginPhase {
    WAITING_FOR_NICKNAME,
    WAITING_FOR_GAMETYPE,
    WAITING_FOR_GAMEREADY,
    END
}
