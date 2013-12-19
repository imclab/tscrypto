package cl.inria.tscrypto.node;

import cl.inria.tscrypto.common.datatypes.KeyShareInfo;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.common.utils.Util;
import com.google.gson.Gson;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

public class H2KeyShareManager implements KeyShareManager, Closeable {
    Connection connection;

    public H2KeyShareManager(NodeConfig config) throws ClassNotFoundException, SQLException {
        String fileName = "node" + config.getNodeId();
        String path = "jdbc:h2:" + fileName;

        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection(path, "sa", "");

        // Create the table if not exists
        try (
                PreparedStatement statement =
                        connection.prepareStatement(
                                "CREATE TABLE IF NOT EXISTS KEYS " +
                                "(ID VARCHAR(255) PRIMARY KEY, " +
                                "KEYSHARE_INFO TEXT)"
                        )
        ) {
            statement.executeUpdate();
            TSLogger.node.debug("Created table if not exists...");
        }

    }

    @Override
    public void addKey(String label, KeyShareInfo key) {

        TSLogger.node.debug("Trying to add key...");
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO KEYS VALUES(?, ?)"))
        {
            statement.setString(1, label);
            statement.setString(2, key.toJson());
            statement.executeUpdate();
            connection.commit();
            TSLogger.keyFactory.debug("Key Added...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeKey(String label) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM KEYS WHERE ID=?")) {
            statement.setString(1, label);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TSPublicKey getPublicKey(String label) {
        KeyShareInfo keyShareInfo = getKeyShareInfo(label);
        return keyShareInfo == null? null : keyShareInfo.getPublicKey();
    }

    @Override
    public KeyShareInfo getKeyShareInfo(String label) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT KEYSHARE_INFO FROM KEYS WHERE ID=?")){
            statement.setString(1, label);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                String json = resultSet.getString(1);
                Gson gson = Util.GsonFactory(true);
                return gson.fromJson(json, KeyShareInfo.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
