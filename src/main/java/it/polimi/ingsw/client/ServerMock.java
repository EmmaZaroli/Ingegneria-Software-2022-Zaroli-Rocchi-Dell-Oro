package it.polimi.ingsw.client;

import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.servercontroller.enums.NicknameStatus;

import java.io.IOException;
import java.net.ServerSocket;

//TODO for testing only. Delete this class
public class ServerMock implements MessageListener {
    public static ServerSocket ss;
    public static Endpoint endpoint;


    public static void main(String[] args) throws IOException {
        ss = new ServerSocket(24000);
        endpoint = new Endpoint(ss.accept());
        endpoint.startReceiving();
    }

    @Override
    public void onMessageReceived(Message message) {
        try {
            if (message instanceof NicknameProposalMessage) {
                endpoint.sendMessage(new NicknameResponseMessage(message.getNickname(), MessageType.NICKNAME_RESPONSE, NicknameStatus.FREE));
            }
            if (message instanceof GametypeRequestMessage) {
                endpoint.sendMessage(new GametypeResponseMessage(message.getNickname(), MessageType.NICKNAME_RESPONSE, true));
                Thread.sleep(2000);

            }
        } catch (Exception e) {
        }
    }
}
