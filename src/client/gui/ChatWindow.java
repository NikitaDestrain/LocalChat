package client.gui;

import client.commandprocessor.ClientCommandSender;
import client.network.ClientNetworkFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ChatWindow extends JFrame {

    public static ChatWindow instance;

    private String loginValue;

    private JPanel allPanel;
    private JScrollPane scrollAll;

    private JTextArea login;
    private JPanel loginPanel;
    private JTextField loginField;
    private JButton submitLoginField;

    private JPanel textPanel;
    private JScrollPane scrollText;
    private JTextArea textArea;

    private JPanel inputTextPanel;
    private JScrollPane inputScrollText;
    private JTextArea inputTextArea;
    private JButton submitSend;

    private ClientNetworkFacade clientNetworkFacade;

    public ChatWindow() {
        instance = this;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(false);
        setTitle("Chat");

        createAllPanel();
        add(allPanel);

        createLoginPanel();
        allPanel.add(loginPanel, BorderLayout.NORTH);

        createTextPanel();
        allPanel.add(textPanel, BorderLayout.CENTER);

        createInputMessagePanel();
        allPanel.add(inputTextPanel, BorderLayout.SOUTH);

        clientNetworkFacade = ClientNetworkFacade.getInstance();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int action = JOptionPane.showConfirmDialog(
                        null, "Do you really want to close the app?",
                        "Warning!",
                        JOptionPane.YES_NO_OPTION);
                if (action == JOptionPane.OK_OPTION) {
                    clientNetworkFacade.finish();
                    System.exit(0);
                }
            }
        });
    }

    private void createAllPanel() {
        allPanel = new JPanel();
        scrollAll = new JScrollPane(allPanel);
        scrollAll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollAll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void createLoginPanel() {
        login = new JTextArea("Login:");
        login.setEditable(false);
        login.setSize(15, 10);

        loginPanel = new JPanel();
        loginField = new JTextField(15);

        submitLoginField = new JButton("Submit");
        submitLoginField.setSize(30, 10);

        submitLoginField.addActionListener((ActionEvent e) -> {
            String login = loginField.getText();
            if (login.trim().equals(""))
                JOptionPane.showMessageDialog(null, "Enter login!");
            else {
                loginValue = login.trim();
                loginField.setEnabled(false);
                submitLoginField.setEnabled(false);
                inputTextArea.setEnabled(true);
                submitSend.setEnabled(true);
                clientNetworkFacade.setLogin(loginValue);
                clientNetworkFacade.connect();
            }
        });

        loginPanel.add(login);
        loginPanel.add(loginField);
        loginPanel.add(submitLoginField);
    }

    private void createTextPanel() {
        textPanel = new JPanel();
        textArea = new JTextArea(20, 70);

        scrollText = new JScrollPane(textArea);
        scrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        textArea.setEditable(false);
        textPanel.add(scrollText);
    }

    private void createInputMessagePanel() {
        inputTextPanel = new JPanel();
        inputTextArea = new JTextArea(4, 50);
        inputTextArea.setEnabled(false);

        inputScrollText = new JScrollPane(inputTextArea);
        inputScrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        inputScrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        submitSend = new JButton("Send");

        submitSend.addActionListener((ActionEvent e) -> {
            try {
                if (inputTextArea.getText().trim().equals(""))
                    JOptionPane.showMessageDialog(null, "Write something for sending!");
                else {
                    ClientCommandSender.getInstance().sendMessage(loginValue + ": " + inputTextArea.getText().trim(),
                            clientNetworkFacade.getDataOutputStream());
                    inputTextArea.setText("");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Try later!");
            }
        });

        submitSend.setSize(30, 10);
        submitSend.setEnabled(false);

        inputTextPanel.add(inputScrollText);
        inputTextPanel.add(submitSend);
    }

    public void updateTextArea(String text) {
        textArea.append(text + "\n");
    }
}
