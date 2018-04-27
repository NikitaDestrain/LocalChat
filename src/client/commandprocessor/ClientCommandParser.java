package client.commandprocessor;

import client.gui.ChatWindow;
import sharedclasses.Constants;
import sharedclasses.model.Command;
import sharedclasses.xml.XmlUtils;

public class ClientCommandParser {

    private static ClientCommandParser instance;

    private ClientCommandParser() {
    }

    public static ClientCommandParser getInstance() {
        if (instance == null) instance = new ClientCommandParser();
        return instance;
    }

    public void parseAndDoAction(String command) {
        Command commandObject = XmlUtils.getInstance().parseToCommand(command);

        switch (commandObject.getType()) {
            case Constants.MESSAGE:
                ChatWindow.instance.updateTextArea(commandObject.getMessage());
        }
    }
}
