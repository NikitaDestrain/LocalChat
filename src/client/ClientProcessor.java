package client;

import client.gui.ChatWindow;
import client.network.ClientNetworkFacade;

import javax.swing.*;

public class ClientProcessor {

    public static void main(String[] args) {
        System.out.println("Client logs: ");
        System.out.println();

        SwingUtilities.invokeLater(() -> {
            new ChatWindow().setVisible(true);
        });
    }
}