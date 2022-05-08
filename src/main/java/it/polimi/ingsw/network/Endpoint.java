package it.polimi.ingsw.network;

import it.polimi.ingsw.servercontroller.MessagesHelper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO check this is working
public class Endpoint {
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Thread receiverThread;

    private final List<MessageListener> messageListeners;
    private final List<DisconnectionListener> disconnectionListeners;

    private boolean isOnline;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Timer disconnectionTimer = new Timer();

    public Endpoint(Socket socket) throws IOException {
        this.messageListeners = new LinkedList<>();
        this.disconnectionListeners = new LinkedList<>();
        this.socket = socket;
        this.in = new ObjectInputStream(this.socket.getInputStream());
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.isOnline = true;
        resetDisconnectionTimer();
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (Exception e) {
            disconnect();
            this.notifyDisconnection();
        }
    }

    public void startReceiving() {
        this.receiverThread = new Thread(() -> {
            while (true) {
                handleIncomingMessage();
            }
        });

        receiverThread.start();
    }

    public Message synchronizedReceive() throws IOException, ClassNotFoundException {
        return (Message) in.readObject();
    }

    //TODO I don't think reflection is the best way to do this
    public Message synchronizedReceive(Class messageClass) {
        Message message = null;//TODO is this ok?
        do {
            try {
                message = synchronizedReceive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!(messageClass.isInstance(message)));
        return message;
    }

    private void handleIncomingMessage() {
        try {
            Message m = (Message) in.readObject();
            resetDisconnectionTimer();
            for (MessageListener l : messageListeners) {
                l.onMessageReceived(m);
            }
        } catch (Exception e) {
            disconnect();
            notifyDisconnection();
        }
    }

    private void resetDisconnectionTimer() {
        this.disconnectionTimer.cancel();
        this.disconnectionTimer = new Timer();
        this.disconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                disconnect();
                notifyDisconnection();
            }
        }, 30 * 1000); //TODO parameterize this
    }

    public void disconnect() {
        isOnline = false;
        try {
            this.receiverThread.interrupt();
            this.socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, MessagesHelper.ERROR_CLOSING_ENDPOINT, e);
        }
    }

    public void notifyDisconnection() {
        for (DisconnectionListener d : this.disconnectionListeners) {
            d.onDisconnect();
        }
    }

    public void addMessageListener(MessageListener l) {
        this.messageListeners.add(l);
    }

    public void removeMessageListener(MessageListener l) {
        this.messageListeners.remove(l);
    }

    public void addDisconnectionListener(DisconnectionListener l) {
        this.disconnectionListeners.add(l);
    }

    public void removeDisconnectionListener(DisconnectionListener l) {
        this.disconnectionListeners.remove(l);
    }
}
