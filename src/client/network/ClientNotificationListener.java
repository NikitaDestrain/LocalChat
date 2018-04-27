package client.network;

import client.commandprocessor.ClientCommandParser;
import sharedclasses.Constants;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Listener for notification from server
 */

public class ClientNotificationListener extends Thread {

    private DataInputStream notificationInputStream;

    public ClientNotificationListener(DataInputStream in) {
        this.notificationInputStream = in;
    }

    public void run() {
        System.out.println("Notification listener starts");
        try {
            while (!isInterrupted()) {
                Thread.sleep(Constants.DEFAULT_SLEEP_TIME);
                if (notificationInputStream.available() > 0) {
                    ClientCommandParser.getInstance().parseAndDoAction(notificationInputStream.readUTF());
                }
            }
            closeInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            closeInputStream();
        }
    }

    private void closeInputStream() {
        try {
            notificationInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
