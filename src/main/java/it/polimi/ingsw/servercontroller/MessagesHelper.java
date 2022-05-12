package it.polimi.ingsw.servercontroller;

public class MessagesHelper {
    private MessagesHelper() {
    }

    public static final String NO_PORT = "Impossibile avviare il server: specificare il numero di porta con il comando --port <porta>";
    public static final String PORT_NOT_AVAILABLE = "La porta selezionata è gia in uso. Riprovare con una porta differente";
    public static final String SERVER_STARTED = "Il server è pronto";
    public static final String ERROR_STARTING_SERVER = "Errore nell'avvio del server:";
    public static final String ERROR_CLOSING_ENDPOINT = "Errore nella chiusura dell'endpoint";
    public static final String ERROR_CREATING_GAME = "Errore nella creazione della partita";

}
