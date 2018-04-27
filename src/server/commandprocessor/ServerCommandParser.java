package server.commandprocessor;

import server.ServerProcessor;
import sharedclasses.Constants;
import sharedclasses.model.Command;
import sharedclasses.xml.XmlUtils;

public class ServerCommandParser {

    private static ServerCommandParser instance;

    private ServerCommandParser() {
    }

    public static ServerCommandParser getInstance() {
        if (instance == null) instance = new ServerCommandParser();
        return instance;
    }

    public void parseAndDoAction(String command) {
        Command commandObject = XmlUtils.getInstance().parseToCommand(command);

        System.out.println(commandObject);

        switch (commandObject.getType()) {
            case Constants.MESSAGE:
                ServerCommandSender.getInstance().sendMessageToAll(commandObject.getMessage());
                break;
            case Constants.DISCONNECT:
                Integer port = Integer.parseInt(commandObject.getMessage());
                String login = ServerProcessor.getInstance().getLoginByPort(port);
                ServerProcessor.getInstance().finishClient(port);
                ServerCommandSender.getInstance().sendMessageToAll(login + Constants.DISCONNECT_MESSAGE);
                break;
        }
    }
}
