package server.network;

import server.commandprocessor.ServerCommandParser;
import server.commandprocessor.ServerCommandSender;
import server.utils.StreamContainer;
import sharedclasses.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Thread for work with the client
 */

public class MonoClientThread extends Thread {

    private String login;
    private int notificationPort;
    private int number;
    private boolean successNotificationConnect;
    private Socket clientDataSocket;
    private Socket notificationSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream notificationOutputStream;
    private StreamContainer streamContainer = StreamContainer.getInstance();

    public MonoClientThread(Socket socket, int notificationPort) {
        this.clientDataSocket = socket;
        this.notificationPort = notificationPort;
        this.number = notificationPort;
        this.successNotificationConnect = true;
    }

    @Override
    public void run() {
        System.out.printf("\nConnection accepted.\n");
        System.out.printf("Client with port %d connected\n", number);
        init();
        connectToNotificationChanel();

        if (successNotificationConnect) {
            try {
                while (dataInputStream.available() < 0) {
                    Thread.sleep(Constants.DEFAULT_SLEEP_TIME);
                }
                login = dataInputStream.readUTF();
                ServerCommandSender.getInstance().sendMessageToAll(login + Constants.CONNECT_MESSAGE);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            commandRelay();
        } else
            finish();
    }

    private void init() {
        try {
            dataOutputStream = new DataOutputStream(clientDataSocket.getOutputStream());
            System.out.println("DataOutputStream created");
            dataInputStream = new DataInputStream(clientDataSocket.getInputStream());
            System.out.println("DataInputStream created");
            streamContainer.addClientDataOutputStream(notificationPort, dataOutputStream);
        } catch (IOException e) {
            System.out.printf("Error! Client with port %d can not be connected!\n", notificationPort);
        }
    }

    private void connectToNotificationChanel() {
        System.out.println("Creating Notification Chanel");
        try {
            dataOutputStream.writeInt(notificationPort);
            dataOutputStream.flush();
            notificationSocket = new Socket("localhost", notificationPort);
            notificationOutputStream = new DataOutputStream(notificationSocket.getOutputStream());
            System.out.println("Notification OutputStream created");
            streamContainer.addNotificationOutputStream(notificationPort, notificationOutputStream);
        } catch (IOException e) {
            successNotificationConnect = false;
        }
    }

    /**
     * Finishes client's thread and close all channels
     */

    public void finish() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            notificationOutputStream.close();
            clientDataSocket.close();
            notificationSocket.close();
            streamContainer.removeNotificationOutputStream(notificationPort);
            streamContainer.removeClientDataOutputStream(notificationPort);
        } catch (IOException e) {
            streamContainer.removeNotificationOutputStream(notificationPort);
            streamContainer.removeClientDataOutputStream(notificationPort);
        }
    }

    private void commandRelay() {
        try {
            while (!isInterrupted()) {
                Thread.sleep(Constants.DEFAULT_SLEEP_TIME);
                if (dataInputStream.available() > 0) {
                    System.out.printf("Client with port %d send: ", number);
                    ServerCommandParser.getInstance().parseAndDoAction(dataInputStream.readUTF());
                }
            }
        } catch (IOException | InterruptedException e) {
            finish();
        }
    }

    public String getLogin() {
        return login;
    }
}
