package it.polimi.ingsw.network;

import it.polimi.ingsw.network.message.Message;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
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

    private final Logger logger = Logger.getLogger(getClass().getName());

    public Endpoint(Socket socket) throws IOException{
        this.messageListeners = new LinkedList<>();
        this.disconnectionListeners = new LinkedList<>();
        this.socket = socket;
        this.in = new ObjectInputStream(this.socket.getInputStream());
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch(Exception e) {
            disconnect();
            this.notifyDisconnection();
        }
    }

    public void startReceiving() {
        this.receiverThread = new Thread(() -> {
            while(true) {
                handleIncomingMessage();
            }
        });

        receiverThread.start();
    }

    private void handleIncomingMessage() {
        try {
            Message m = (Message) in.readObject();
            for(MessageListener l: messageListeners) {
                l.onMessageReceived(m);
            }
        } catch (Exception e) {
            disconnect();
            notifyDisconnection();
        }
    }

    public void disconnect() {
        try {
            this.receiverThread.interrupt();
            this.socket.close();
        } catch(IOException e) {
            logger.log(Level.SEVERE, "Error while closing an Endpoint", e);
        }
    }

    public void notifyDisconnection() {
        for(DisconnectionListener d: this.disconnectionListeners) {
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
