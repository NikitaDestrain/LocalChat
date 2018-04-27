package client.commandprocessor;

import sharedclasses.Constants;
import sharedclasses.factories.CommandFactory;
import sharedclasses.model.Command;
import sharedclasses.xml.XmlUtils;

import java.io.DataOutputStream;
import java.io.IOException;

public class ClientCommandSender {

    private static ClientCommandSender instance;
    private XmlUtils xmlUtils = XmlUtils.getInstance();

    private ClientCommandSender() {
    }

    public static ClientCommandSender getInstance() {
        if (instance == null) instance = new ClientCommandSender();
        return instance;
    }

    private void sendCommand(String type, String message, DataOutputStream out) throws IOException {
        Command command = CommandFactory.createCommand(type, message);
        out.writeUTF(xmlUtils.commandToXmlString(command));
        out.flush();
    }

    public void sendMessage(String message, DataOutputStream out) throws IOException {
        sendCommand(Constants.MESSAGE, message, out);
    }

    public void sendDisconnect(String port, DataOutputStream out) throws IOException {
        sendCommand(Constants.DISCONNECT, port, out);
    }
}
