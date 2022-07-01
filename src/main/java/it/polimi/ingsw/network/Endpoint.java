package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.PingMessage;
import it.polimi.ingsw.servercontroller.MessagesHelper;
import it.polimi.ingsw.utils.ApplicationConstants;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EndPoint
 */
public class Endpoint {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    private ReceiverThread receiverThread;

    private final List<MessageListener> messageListeners;
    private final List<DisconnectionListener> disconnectionListeners;

    private boolean isOnline;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Timer disconnectionTimer = new Timer();
    private final Timer pingTimer = new Timer();
    ExecutorService executor = Executors.newSingleThreadExecutor();


    /**
     * Default constructor
     * @param socket the socket
     * @param serverSide true if the endpoint is for the server, false if it's for the client
     * @throws IOException signals that an I/O exception of some sort has occurred
     */
    public Endpoint(Socket socket, boolean serverSide) throws IOException {
        this.messageListeners = new LinkedList<>();
        this.disconnectionListeners = new LinkedList<>();
        this.socket = socket;

        InputStream input = this.socket.getInputStream();
        OutputStream output = this.socket.getOutputStream();
        output.flush();


        if (serverSide) {
            this.in = new ObjectInputStream(input);
            this.out = new ObjectOutputStream(output);
        } else {
            this.out = new ObjectOutputStream(output);
            this.in = new ObjectInputStream(input);
        }

        this.isOnline = true;
        resetDisconnectionTimer();
        startPinging();
    }

    /**
     *
     * @return true if the endpoint is online, false otherwise
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     *
     * @param message the message to send
     */
    public void sendMessage(Message message) {
        if(isOnline){
            try {
                out.writeObject(message);
                out.flush();
            } catch (Exception e) {
                disconnect();
                this.notifyDisconnection();
            }
        }
    }

    /**
     * Instantiates a new thread to read incoming messages
     */
    public void startReceiving() {
        this.receiverThread = new ReceiverThread();
        receiverThread.startThread();
    }

    /**
     * Close the thread previously instantiated to read incoming messages
     */
    private void stopReceiving() {
        this.receiverThread.stopThread();
    }

    /*
    public Message synchronizedReceive() throws IOException, ClassNotFoundException {
        Message message = (Message) in.readObject();
        resetDisconnectionTimer();
        return message;

    }

    public Message synchronizedReceive(Class messageClass) {
        Message message = null;
        do {
            try {
                message = synchronizedReceive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!(messageClass.isInstance(message)));
        return message;
    }*/

    /**
     * Reads the incoming message and notify the registered messageListeners with the message
     */
    private void handleIncomingMessage() {
        try {
            Message m = (Message) in.readObject();
            resetDisconnectionTimer();
            if (m.getType() != MessageType.PING) {
                executor.submit(() -> {
                    List<MessageListener> temporaryList = new ArrayList<>(messageListeners);
                    for(MessageListener l : temporaryList){
                        l.onMessageReceived(m);
                    }
                });
            }
            else{
                //System.out.println("RECIVED PING");
            }
        } catch (Exception e) {
            disconnect();
            notifyDisconnection();
        }
    }

    /**
     * Resets the disconnection timer
     */
    private void resetDisconnectionTimer() {
        this.disconnectionTimer.cancel();
        this.disconnectionTimer = new Timer();
        this.disconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                disconnect();
                notifyDisconnection();
            }
        }, ApplicationConstants.DISCONNECTION_TIMER_PING);
    }

    /**
     * Sends at fixed periods a PingMessage to the player, checking if he's still online
     */
    private void startPinging() {
        this.pingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendMessage(new PingMessage(MessageType.PING));
                //System.out.println("SEND PING");
            }
        }, ApplicationConstants.PING_PERIOD, ApplicationConstants.PING_PERIOD);
    }

    private void stopPinging(){
        this.pingTimer.cancel();
    }

    /**
     * Called when the player is disconnected
     */
    public void disconnect() {
        isOnline = false;
        try {
            this.stopPinging();
            this.receiverThread.stopThread();
            this.socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, MessagesHelper.ERROR_CLOSING_ENDPOINT, e);
        }
    }

    /**
     * Notify the disconnection to all the disconnectionListeners
     */
    public void notifyDisconnection() {
        for (DisconnectionListener d : this.disconnectionListeners) {
            d.onDisconnect(this);
        }
    }

    /**
     * Adds a messageListener
     * @param l the messageListener
     */
    public void addMessageListener(MessageListener l) {
        this.messageListeners.add(l);
    }

    /**
     * Remove a messageListener
     * @param l the messageListener
     */
    public void removeMessageListener(MessageListener l) {
        this.messageListeners.remove(l);
    }

    /**
     * Adds a disconnectionListener
     * @param l the disconnectionListener
     */
    public void addDisconnectionListener(DisconnectionListener l) {
        this.disconnectionListeners.add(l);
    }

    /**
     * Remove a disconnectionListener
     * @param l the disconnectionListener
     */
    public void removeDisconnectionListener(DisconnectionListener l) {
        this.disconnectionListeners.remove(l);
    }

    /**
     * Class used for handling incoming messages asynchronously from the rest of the Endpoint class
     */
    private class ReceiverThread {
        private final Thread thread;
        private boolean stopFlag;

        public ReceiverThread() {
            this.thread = new Thread(() -> {
                while (!stopFlag) {
                    handleIncomingMessage();
                }
            });
        }

        public void startThread() {
            this.thread.start();
        }

        public void stopThread() {
            this.stopFlag = true;
            this.thread.interrupt();
        }
    }
}
