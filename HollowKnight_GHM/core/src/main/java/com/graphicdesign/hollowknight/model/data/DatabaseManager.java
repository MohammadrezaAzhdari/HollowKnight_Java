package com.graphicdesign.hollowknight.model.data;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final String url = "jdbc:sqlite:hollowKnight.db";

    public static DatabaseManager getInstance() {
        if(instance == null) {
            instance = new DatabaseManager();
            return instance;
        }
        return instance;
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS Setting (" +
            "id INTEGER PRIMARY KEY," +
            " musicVolume REAL," +
            " isMusicMute INTEGER," +
            " isSfxMute INTEGER," +
            " brightness REAL)";

        try (
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement()
            )
        {
            statement.execute(sql);
        }
        catch (SQLException e) {
            System.err.println("Error creating tables :" + e.getMessage());
        }
    }

    public void saveSetting(SettingData data) {
        String sql = "INSERT OR REPLACE INTO Setting (id, musicVolume, isMusicMute, isSfxMute, brightness) VALUES (1, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setFloat(1, data.getMusicVolume());
            statement.setInt(2, data.isMusicMute() ? 1 : 0);
            statement.setInt(3, data.isSfxMute() ? 1 : 0);
            statement.setFloat(4, data.getBrightness());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.err.println("unable to save setting : " + e.getMessage());
        }

    }

    public SettingData loadSetting() {
        SettingData data = new SettingData();
        String sql = "SELECT * FROM Setting WHERE id = 1";
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql) )
        {
            result.next();
            data.setMusicVolume(result.getFloat("musicVolume"));
            data.setMusicMute(result.getInt("isMusicMute") == 1);
            data.setSfxMute(result.getInt("isSfxMute") == 1);
            data.setBrightness(result.getFloat("brightness"));

            return data;
        }
        catch (SQLException e) {
            System.err.println("Unable load setting : " + e.getMessage());
        }

        data.setMusicVolume(100f);
        data.setMusicMute(false);
        data.setSfxMute(false);
        data.setBrightness(100f);
        return data;
    }


}
