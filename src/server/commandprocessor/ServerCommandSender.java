package server.commandprocessor;

import server.utils.StreamContainer;
import sharedclasses.Constants;
import sharedclasses.factories.CommandFactory;
import sharedclasses.model.Command;
import sharedclasses.xml.XmlUtils;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerCommandSender {

    private static ServerCommandSender instance;
    private XmlUtils xmlUtils = XmlUtils.getInstance();

    private ServerCommandSender() {
    }

    public static synchronized ServerCommandSender getInstance() {
        if (instance == null) instance = new ServerCommandSender();
        return instance;
    }

    private synchronized void sendCommand(String type, String message, DataOutputStream out) throws IOException {
        Command command = CommandFactory.createCommand(type, message);
        out.writeUTF(xmlUtils.commandToXmlString(command));
        out.flush();
    }

    public synchronized void sendMessage(String message, DataOutputStream out) throws IOException {
        sendCommand(Constants.MESSAGE, message, out);
    }

    public synchronized void sendMessageToAll(String message) {
        for (DataOutputStream dos : StreamContainer.getInstance().getClientNotificationOutputStreams()) {
            try {
                ServerCommandSender.getInstance().sendMessage(message, dos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
