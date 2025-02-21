package com.Guess.ReportsPlus.util.Report.Database;

import com.Guess.ReportsPlus.util.Misc.LogUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

public class DynamicDB {

    private final String dbFilePath;
    private final String tableName;
    private final String primaryKeyColumn;
    private final Map<String, String> columnsDefinition;
    private Connection connection;

    public DynamicDB(String dbFilePath, String tableName, String primaryKeyColumn, Map<String, String> columnsDefinition) {
        this.dbFilePath = dbFilePath + ".db";
        this.tableName = tableName;
        this.primaryKeyColumn = primaryKeyColumn;

        this.columnsDefinition = new LinkedHashMap<>(columnsDefinition);
        if (!this.columnsDefinition.containsKey(primaryKeyColumn)) {
            logError("Columns definition must include the primary key column: " + primaryKeyColumn, new IllegalArgumentException());
        }
    }

    public static boolean isValidDatabase(String dbFilePath, String name) {
        String url = "jdbc:sqlite:" + dbFilePath;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn == null) {
                log("Database; Failed to establish connection to: " + name, LogUtils.Severity.ERROR);
                return false;
            }
            boolean hasLayoutTable = tableExists(dbFilePath, "layout");
            boolean hasDataTable = tableExists(dbFilePath, "data");
            log("Database; [" + name + "] has layout table: " + hasLayoutTable + " has data table: " + hasDataTable, LogUtils.Severity.INFO);
            boolean isValid = hasLayoutTable && hasDataTable;

            if (!isValid) {
                log("Database; [" + name + "] Database missing required tables", LogUtils.Severity.WARN);
            }

            return isValid;
        } catch (SQLException e) {
            log("Database; [" + name + "] SQL Exception while checking database: ", LogUtils.Severity.ERROR);
            e.printStackTrace();
            return false;
        }
    }

    public static Map<String, String> getTableColumnsDefinition(String dbFilePath, String tableName) throws SQLException {
        String fullPath = dbFilePath.endsWith(".db") ? dbFilePath : dbFilePath + ".db";
        Map<String, String> columnsDefinition = new LinkedHashMap<>();

        String sql = "SELECT sql FROM sqlite_master WHERE type='table' AND name=?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + fullPath); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String createTableSql = rs.getString("sql");
                    parseColumnDefinitions(createTableSql, columnsDefinition);
                } else {
                    throw new SQLException("Table '" + tableName + "' does not exist in the database.");
                }
            }
        }

        return columnsDefinition;
    }

    private static void parseColumnDefinitions(String createTableSql, Map<String, String> columnsDefinition) {
        int start = createTableSql.indexOf('(');
        int end = createTableSql.lastIndexOf(')');
        if (start == -1 || end == -1) return;

        String columnsPart = createTableSql.substring(start + 1, end).trim();
        String[] definitions = columnsPart.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String def : definitions) {
            def = def.trim();
            if (def.startsWith("PRIMARY KEY") || def.startsWith("FOREIGN KEY") || def.startsWith("CHECK")) {
                continue;
            }

            // Handle quoted column names
            String columnName;
            String columnDef;
            if (def.startsWith("\"")) {
                int endQuote = def.indexOf("\"", 1);
                columnName = def.substring(1, endQuote);
                columnDef = def.substring(endQuote + 1).trim();
            } else {
                int firstSpace = def.indexOf(" ");
                columnName = def.substring(0, firstSpace);
                columnDef = def.substring(firstSpace + 1).trim();
            }

            columnsDefinition.put(columnName, columnDef);
        }
    }

    public static boolean tableExists(String dbFilePath, String tableName) throws SQLException {
        String fullPath = dbFilePath.endsWith(".db") ? dbFilePath : dbFilePath + ".db";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + fullPath); PreparedStatement ps = conn.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name=?")) {
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static String getPrimaryKeyColumn(String dbFilePath, String tableName) throws SQLException {
        String fullPath = dbFilePath.endsWith(".db") ? dbFilePath : dbFilePath + ".db";
        List<String> pkColumns = new ArrayList<>();

        String pragmaSql = "PRAGMA table_info(" + tableName + ")";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + fullPath); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(pragmaSql)) {

            while (rs.next()) {

                int pkIndex = rs.getInt("pk");
                if (pkIndex > 0) {
                    String columnName = rs.getString("name");

                    pkColumns.add(pkIndex - 1, columnName);
                }
            }
        }

        if (pkColumns.isEmpty()) {
            throw new SQLException("Table '" + tableName + "' has no primary key.");
        }

        return String.join(", ", pkColumns);
    }

    public boolean initDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
            log("Connected to database: " + dbFilePath, LogUtils.Severity.INFO);
            createTableIfNotExists();
            return true;
        } catch (Exception e) {
            logError("Error connecting to database", e);
            return false;
        }
    }

    private String escapeIdentifier(String identifier) {
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }

    private void createTableIfNotExists() {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sql.append(escapeIdentifier(tableName)).append(" (");

        for (Map.Entry<String, String> entry : columnsDefinition.entrySet()) {
            String escapedColumn = escapeIdentifier(entry.getKey());
            sql.append(escapedColumn).append(" ").append(entry.getValue());

            if (entry.getKey().equals(primaryKeyColumn)) {
                sql.append(" PRIMARY KEY");
            }
            sql.append(", ");
        }

        sql.setLength(sql.length() - 2);
        sql.append(");");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql.toString());
            log("Created table: " + tableName, LogUtils.Severity.INFO);
        } catch (SQLException e) {
            logError("Error creating table: " + e.getMessage(), e);
        }
    }

    public void addOrReplaceRecord(Map<String, Object> record) throws SQLException {
        validateRecord(record);
        Object pkValue = record.get(primaryKeyColumn);
        if (pkValue == null) {
            logError("Record must contain a value for primary key column: " + primaryKeyColumn, new IllegalArgumentException());
        }

        String checkSQL = "SELECT COUNT(*) FROM " + escapeIdentifier(tableName) + " WHERE " + escapeIdentifier(primaryKeyColumn) + " = ?";

        try (PreparedStatement ps = connection.prepareStatement(checkSQL)) {
            ps.setObject(1, pkValue);
            ResultSet rs = ps.executeQuery();
            boolean exists = false;
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            rs.close();
            if (exists) {
                updateRecord(record);
            } else {
                insertRecord(record);
            }
        }
        log("Record: " + record + " added or replaced in table: " + tableName, LogUtils.Severity.INFO);
    }

    private void insertRecord(Map<String, Object> record) throws SQLException {
        StringBuilder columnsPart = new StringBuilder();
        StringBuilder valuesPart = new StringBuilder();
        List<Object> values = new ArrayList<>();

        for (String col : columnsDefinition.keySet()) {
            if (record.containsKey(col)) {
                columnsPart.append(escapeIdentifier(col)).append(", ");
                valuesPart.append("?, ");
                values.add(record.get(col));
            }
        }

        if (columnsPart.length() > 0) {
            columnsPart.setLength(columnsPart.length() - 2);
            valuesPart.setLength(valuesPart.length() - 2);
        }
        String sql = "INSERT INTO " + escapeIdentifier(tableName) + " (" + columnsPart + ") VALUES (" + valuesPart + ")";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            ps.executeUpdate();
        }
    }

    private void updateRecord(Map<String, Object> record) throws SQLException {
        StringBuilder setClause = new StringBuilder();
        List<Object> values = new ArrayList<>();

        for (String col : columnsDefinition.keySet()) {
            if (col.equals(primaryKeyColumn)) {
                continue;
            }
            if (record.containsKey(col)) {
                setClause.append(escapeIdentifier(col)).append(" = ?, ");
                values.add(record.get(col));
            }
        }
        if (setClause.length() > 0) {
            setClause.setLength(setClause.length() - 2);
        }
        String sql = "UPDATE " + escapeIdentifier(tableName) + " SET " + setClause + " WHERE " + escapeIdentifier(primaryKeyColumn) + " = ?";
        values.add(record.get(primaryKeyColumn));
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            int affected = ps.executeUpdate();
            if (affected == 0) {
                logError("No record updated; record not found for primary key: " + primaryKeyColumn, new SQLException());
            }
        }
    }

    public Map<String, Object> getRecord(Object pkValue) throws SQLException {
        String sql = "SELECT * FROM " + escapeIdentifier(tableName) + " WHERE " + escapeIdentifier(primaryKeyColumn) + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, pkValue);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> record = new LinkedHashMap<>();
                for (String col : columnsDefinition.keySet()) {
                    record.put(col, rs.getObject(col));
                }
                rs.close();
                return record;
            }
            rs.close();
            return null;
        }
    }

    public List<Map<String, Object>> getAllRecords() throws SQLException {
        List<Map<String, Object>> records = new ArrayList<>();
        String sql = "SELECT * FROM " + escapeIdentifier(tableName);
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> record = new LinkedHashMap<>();
                for (String col : columnsDefinition.keySet()) {
                    record.put(col, rs.getObject(col));
                }
                records.add(record);
            }
        }
        return records;
    }

    private void validateRecord(Map<String, Object> record) {
        for (Map.Entry<String, String> entry : columnsDefinition.entrySet()) {
            String column = entry.getKey();
            String expectedType = entry.getValue().toUpperCase();
            Object value = record.get(column);

            if (value == null) {
                continue;
            }

            switch (expectedType) {
                case "INTEGER":
                    if (!(value instanceof Number)) {
                        logError("Column '" + column + "' expects an INTEGER, but got " + value.getClass().getSimpleName(), new IllegalArgumentException());
                    }
                    break;
                case "REAL":
                    if (!(value instanceof Number)) {
                        logError("Column '" + column + "' expects a REAL, but got " + value.getClass().getSimpleName(), new IllegalArgumentException());
                    }
                    break;
                case "TEXT":
                    if (!(value instanceof String)) {
                        logError("Column '" + column + "' expects TEXT, but got " + value.getClass().getSimpleName(), new IllegalArgumentException());
                    }
                    break;
                case "BLOB":
                    if (!(value instanceof byte[])) {
                        logError("Column '" + column + "' expects a BLOB (byte[]), but got " + value.getClass().getSimpleName(), new IllegalArgumentException());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            log("Database connection closed", LogUtils.Severity.INFO);
        }
    }

    public String getTableName() {
        return tableName;
    }
}
