package client.network;

import client.commandprocessor.ClientCommandSender;
import sharedclasses.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Facade for connection with server
 */

public class ClientNetworkFacade {
    private String login;
    private Integer notificationPort;
    private Integer serverPort;
    private boolean successConnect;
    private Socket clientDataSocket;
    private Socket notificationSenderSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ServerSocket notificationSocket;
    private DataInputStream notificationInputStream;
    private static ClientNetworkFacade instance;
    private ClientNotificationListener clientNotificationListener;

    private ClientNetworkFacade() {
        successConnect = false;
    }

    public static ClientNetworkFacade getInstance() {
        if (instance == null) instance = new ClientNetworkFacade();
        return instance;
    }

    /**
     * Connects to server, creates data channels and call method for create notification channels with listener
     *
     * @return 0 or 1 (success or not success connection)
     */

    public int connect() {
        try {
            if (successConnect)
                return 0;
            serverPort = Constants.DEFAULT_SERVER_PORT;
            clientDataSocket = new Socket("localhost", serverPort);
            dataOutputStream = new DataOutputStream(clientDataSocket.getOutputStream());
            System.out.println("DataOutputStream created");
            dataInputStream = new DataInputStream(clientDataSocket.getInputStream());
            System.out.println("DataInputStream created");
            while (dataInputStream.available() < 0) {
                try {
                    Thread.sleep(Constants.DEFAULT_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notificationPort = dataInputStream.readInt();
            System.out.println("Port: " + notificationPort);
            createNotificationChanel(notificationPort);
            dataOutputStream.writeUTF(login);
            dataOutputStream.flush();
            return 0;
        } catch (IOException e) {
            System.out.println("Server is offline!");
        }
        return 1;
    }

    private void createNotificationChanel(int port) {
        try {
            System.out.println("Creating chanel for Notifications");
            notificationSocket = new ServerSocket(port);
            notificationSenderSocket = notificationSocket.accept();
            System.out.println("Connection accepted.");
            notificationInputStream = new DataInputStream(notificationSenderSocket.getInputStream());
            System.out.println("Notification InputStream created");
            clientNotificationListener = new ClientNotificationListener(notificationInputStream);
            clientNotificationListener.start();
            successConnect = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finishes facade and closes all channels
     */

    public void finish() {
        try {
            ClientCommandSender.getInstance().sendDisconnect(notificationPort.toString(), dataOutputStream);
            clientNotificationListener.interrupt();
            notificationSenderSocket.close();
            dataInputStream.close();
            dataOutputStream.close();
            clientDataSocket.close();
            System.out.println("Closing notification connections & channels - DONE.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public Integer getNotificationPort() {
        return notificationPort;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}