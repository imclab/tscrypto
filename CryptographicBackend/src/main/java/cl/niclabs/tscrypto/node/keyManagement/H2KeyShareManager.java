/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.tscrypto.node.keyManagement;

import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.Util;
import cl.niclabs.tscrypto.node.NodeConfig;
import com.google.gson.Gson;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

public class H2KeyShareManager implements KeyShareManager, Closeable {
    private Connection connection;

    public H2KeyShareManager() throws ClassNotFoundException, SQLException {
        NodeConfig config = NodeConfig.getInstance();
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
            TSLogger.keyDealer.debug("Key Added...");
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
