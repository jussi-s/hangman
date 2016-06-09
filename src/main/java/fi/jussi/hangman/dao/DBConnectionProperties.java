package fi.jussi.hangman.dao;

/**
 * Created by Jussi on 9.6.2016.
 *
 * @author Jussi
 * This class provides the database connection properties.
 */
public class DBConnectionProperties {

    private String host = "";
    private int port;

    /**
     * Creates a database connection properties instance.
     * @param host The host of the database.
     * @param port The port of the database.
     */
    public DBConnectionProperties(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Retrieves the host of the database.
     * @return The host of the database.
     */
    public String getHost() {
        return host;
    }

    /**
     * Retrieves the port of the database.
     * @return The port of the database.
     */
    public int getPort() {
        return port;
    }
}
