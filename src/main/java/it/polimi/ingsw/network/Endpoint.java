package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.PingMessage;
import it.polimi.ingsw.servercontroller.MessagesHelper;
import it.polimi.ingsw.utils.ApplicationConstants;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Endpoint {
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private ReciverThread receiverThread;

    private final List<MessageListener> messageListeners;
    private final List<DisconnectionListener> disconnectionListeners;

    private boolean isOnline;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Timer disconnectionTimer = new Timer();
    private Timer pingTimer = new Timer();

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
        //resetDisconnectionTimer();
        //startPinging();
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void sendMessage(Message message) {
        if(isOnline){
            try {
                out.writeObject(message);
                out.flush();
            } catch (Exception e) {
                disconnect("SCRITTURA");
                this.notifyDisconnection();
            }
        }
    }

    public void startReceiving() {
        this.receiverThread = new ReciverThread();

        receiverThread.startThread();
    }

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

    private void handleIncomingMessage() {
        try {
            Message m = (Message) in.readObject();
            //resetDisconnectionTimer();
            if (m.getType() != MessageType.PING) {
                synchronized (messageListeners){
                    List<MessageListener> temporaryList = new ArrayList<>(messageListeners);
                    for(MessageListener l : temporaryList){
                        l.onMessageReceived(m);
                    }
                }
            }
        } catch (Exception e) {
            disconnect(e.getMessage());
            notifyDisconnection();
        }
    }

    private void resetDisconnectionTimer() {
        this.disconnectionTimer.cancel();
        this.disconnectionTimer = new Timer();
        this.disconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                disconnect("TIMER SCADUTO");
                notifyDisconnection();
            }
        }, ApplicationConstants.DISCONNECTION_TIMER_PING);
    }

    private void startPinging() {
        this.pingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendMessage(new PingMessage(MessageType.PING));
            }
        }, ApplicationConstants.PING_PERIOD, ApplicationConstants.PING_PERIOD);
    }

    public void disconnect(String motivo) {
        System.out.println("DISCONNESSIONE " + motivo);
        isOnline = false;
        try {
            this.receiverThread.stopThread();
            this.socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, MessagesHelper.ERROR_CLOSING_ENDPOINT, e);
        }


    }

    public void notifyDisconnection() {
        for (DisconnectionListener d : this.disconnectionListeners) {
            d.onDisconnect(this);
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

    private class ReciverThread {
        private Thread thread;
        private boolean stopFlag;

        public ReciverThread() {
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
