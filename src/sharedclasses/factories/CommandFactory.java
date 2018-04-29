package sharedclasses.factories;

import sharedclasses.model.Command;

public class CommandFactory {

    public synchronized static Command createCommand(String type, String message) {
        return new Command(type, message);
    }
}
