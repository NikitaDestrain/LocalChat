package server.utils;

import java.io.DataOutputStream;
import java.util.*;

/**
 * Container for client's streams
 */

public class StreamContainer {

    private static StreamContainer instance;
    private Map<Integer, DataOutputStream> clientNotificationOutputStreams;
    private Map<Integer, DataOutputStream> clientDataOutputStreams;

    private StreamContainer() {
        clientNotificationOutputStreams = new HashMap<>();
        clientDataOutputStreams = new HashMap<>();
    }

    public static StreamContainer getInstance() {
        if (instance == null) instance = new StreamContainer();
        return instance;
    }

    /**
     * Returns list of actual channels for notification to clients
     *
     * @return UnmodifiableList
     */

    public List<DataOutputStream> getClientNotificationOutputStreams() {
        LinkedList<DataOutputStream> list = new LinkedList<>(clientNotificationOutputStreams.values());
        return Collections.unmodifiableList(list);
    }

    /**
     * Returns list of actual channels for communicate to clients
     *
     * @return UnmodifiableList
     */

    public List<DataOutputStream> getClientDataOutputStreams() {
        LinkedList<DataOutputStream> list = new LinkedList<>(clientDataOutputStreams.values());
        return Collections.unmodifiableList(list);
    }

    public void removeNotificationOutputStream(Integer key) {
        clientNotificationOutputStreams.remove(key);
    }

    public void addNotificationOutputStream(Integer key, DataOutputStream dataOutputStream) {
        clientNotificationOutputStreams.put(key, dataOutputStream);
    }

    public void addClientDataOutputStream(Integer key, DataOutputStream dataOutputStream) {
        clientDataOutputStreams.put(key, dataOutputStream);
    }

    public void removeClientDataOutputStream(Integer key) {
        clientDataOutputStreams.remove(key);
    }

    public DataOutputStream getDataOutputStream(int key) {
        return clientDataOutputStreams.get(key);
    }
}
