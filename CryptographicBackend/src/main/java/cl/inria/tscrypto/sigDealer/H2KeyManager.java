package cl.inria.tscrypto.sigDealer;

import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.KeyMetaInfo;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.common.utils.Util;
import com.google.gson.Gson;

import java.io.Closeable;
import java.io.IOException;

import java.sql.*;

public class H2KeyManager implements KeyManager, Closeable {
    Connection connection;

    public H2KeyManager() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:sd", "sa", "");

        // Create the table if not exists
        try (
            PreparedStatement statement =
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS KEYS " +
                    "(ID VARCHAR(255) PRIMARY KEY, " +
                    "PUBLIC_KEY TEXT, " +
                    "KEY_METAINFO TEXT)")
        ) {
            statement.executeUpdate();
            TSLogger.keyFactory.debug("Created table if not exists...");
        }
    }

    @Override
    public void addKey(KeyInfo key) {
        String label = key.getKeyMetaInfo().getAlias();
        KeyMetaInfo keyMetaInfo = key.getKeyMetaInfo();
        TSPublicKey publicKey = key.getPublicKey();

        TSLogger.keyFactory.debug("Trying to add key...");
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO KEYS VALUES(?, ?, ?)"))
        {
            statement.setString(1, label);
            statement.setString(2, publicKey.toJson());
            statement.setString(3, keyMetaInfo.toJson());
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
    public boolean containsKey(String label) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM KEYS WHERE ID=?")) {
            statement.setString(1, label);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TSPublicKey getPublicKey(String label) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT PUBLIC_KEY FROM KEYS WHERE ID=?")){
            statement.setString(1, label);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                String json = resultSet.getString(1);
                Gson gson = Util.GsonFactory(true);
                return gson.fromJson(json, TSPublicKey.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public KeyMetaInfo getKeyMetaInfo(String label) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT KEY_METAINFO FROM KEYS WHERE ID=?")){
            statement.setString(1, label);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                String json = resultSet.getString(1);
                Gson gson = Util.GsonFactory(true);
                return gson.fromJson(json, KeyMetaInfo.class);
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
