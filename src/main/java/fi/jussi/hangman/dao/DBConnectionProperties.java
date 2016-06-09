package fi.jussi.hangman.dao;

/**
 * Created by Jussi on 9.6.2016.
 */
public class DBConnectionProperties {

    private String host = "";
    private int port;

    public DBConnectionProperties(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
