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

    private DatabaseManager() {
        createTables();
    }

    private void createTables() {
        String settingSql = "CREATE TABLE IF NOT EXISTS Setting (" +
            "id INTEGER PRIMARY KEY," +
            " musicVolume REAL," +
            " isMusicMute INTEGER," +
            " isSfxMute INTEGER," +
            " brightness REAL)";

        String gameSql = "CREATE TABLE IF NOT EXISTS Game (" +
            "slotId INTEGER PRIMARY KEY," +
            " knightPosX REAL," +
            " knightPosY REAL," +
            " health INTEGER," +
            " soulAmount INTEGER" +
            // TODO -> Add more!
            ")";

        String enemySql = "CREATE TABLE IF NOT EXISTS Enemy (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " slotId INTEGER," +
            " posX REAL," +
            " posY REAL," +
            " type TEXT," +
            " FOREIGN KEY(slotId) REFERENCES Game(slotId)" +
            ")";
        try (
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement()
            )
        {
            statement.execute(settingSql);
            statement.execute(gameSql);
            statement.execute(enemySql);
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

    public void saveGame(GameData data) {
        String sql = "INSERT OR REPLACE INTO Game (slotId, knightPosX, knightPosY, health, soulAmount) VALUES (?, ?, ?, ?, ?)";
        String clearEnemiesSql = "DELETE FROM Enemy WHERE slotId = ?";
        String insertEnemySql = "INSERT INTO Enemy (slotId, posX, posY, type) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url))
        {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, data.getSlotId());
                statement.setFloat(2, data.getKnightPosX());
                statement.setFloat(3, data.getKnightPosY());
                statement.setInt(4, data.getHealth());
                statement.setInt(5, data.getSoulAmount());
                statement.executeUpdate();
            }

            try (PreparedStatement clearStatement = connection.prepareStatement(clearEnemiesSql)){
                clearStatement.setInt(1, data.getSlotId());
                clearStatement.executeUpdate();
            }
            try (PreparedStatement enemyStatement = connection.prepareStatement(insertEnemySql)){
                for(EnemyData enemy : data.getEnemiesData()) {
                    enemyStatement.setInt(1, data.getSlotId());
                    enemyStatement.setFloat(2, enemy.x);
                    enemyStatement.setFloat(3, enemy.y);
                    enemyStatement.setString(4, enemy.type);
                    enemyStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Unable to save game :" + e.getMessage());
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

    public GameData loadGame(int slotId) {
        String sql = "SELECT * FROM Game WHERE slotId = ?";
        String enemySql = "SELECT * FROM Enemy WHERE slotId = ?";

        try (Connection connection = DriverManager.getConnection(url)) {
            GameData data = new GameData();
            boolean found = false;


            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, slotId);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    data.setSlotId(result.getInt("slotId"));
                    data.setKnightPosX(result.getFloat("knightPosX"));
                    data.setKnightPosY(result.getFloat("knightPosY"));
                    data.setHealth(result.getInt("health"));
                    data.setSoulAmount(result.getInt("soulAmount"));
                    found = true;
                }
            }

            if (!found) return null;

            // Load Enemies
            try (PreparedStatement statement = connection.prepareStatement(enemySql)) {
                statement.setInt(1, slotId);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    data.getEnemiesData().add(new EnemyData(
                        result.getFloat("posX"),
                        result.getFloat("posY"),
                        result.getString("type")
                    ));

                }
            }
            return data;
        } catch (SQLException e) {
            System.err.println("Unable to load game : " + e.getMessage());
        }
        return null;
    }
}
