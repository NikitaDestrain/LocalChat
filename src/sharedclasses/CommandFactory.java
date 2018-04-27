package sharedclasses;

public class CommandFactory {

    public static Command createCommand(String type, String message) {
        return new Command(type, message);
    }
}
