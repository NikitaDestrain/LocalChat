package server.network;

import server.ServerProcessor;
import server.utils.PortGenerator;
import sharedclasses.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Facade for managing client's threads
 */

public class ServerNetworkFacade extends Thread {
    private int serverPort;
    private ServerSocket serverDataSocket;
    private Socket clientDataSocket;
    private ExecutorService executeIt;
    private ServerProcessor serverProcessor = ServerProcessor.getInstance();

    public ServerNetworkFacade() {
    }

    @Override
    public void run() {
        System.out.println("Server logs:");
        serverPort = Constants.DEFAULT_SERVER_PORT;
        start(serverPort, Constants.DEFAULT_MAX_COUNT_CLIENTS);
        while (!isInterrupted()) {
            try {
                clientDataSocket = serverDataSocket.accept();
                int clientNotificationPort = PortGenerator.getInstance().createPort();
                MonoClientThread monoClientThread = new MonoClientThread(clientDataSocket, clientNotificationPort);
                executeIt.execute(monoClientThread);
                serverProcessor.addClient(clientNotificationPort, monoClientThread);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void start(int port, int nThreads) {
        try {
            executeIt = Executors.newFixedThreadPool(nThreads);
            serverDataSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finishes server facade and closes socket
     */

    public void finish() {
        try {
            serverDataSocket.close();
            executeIt.shutdown();
            System.out.println("Correct finish");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
