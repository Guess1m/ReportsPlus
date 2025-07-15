package com.Guess.ReportsPlus.util.Report.Database;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DynamicDB {

	private final String dbFilePath;
	private final String tableName;
	private final String primaryKeyColumn;
	private final Map<String, String> columnsDefinition;
	private Connection connection;

	public DynamicDB(String dbFilePath, String tableName, String primaryKeyColumn,
			Map<String, String> columnsDefinition) {
		this.dbFilePath = dbFilePath + ".db";
		this.tableName = tableName;
		this.primaryKeyColumn = primaryKeyColumn;

		this.columnsDefinition = new LinkedHashMap<>(columnsDefinition);
		if (!this.columnsDefinition.containsKey(primaryKeyColumn)) {
			logError("Columns definition must include the primary key column: " + primaryKeyColumn,
					new IllegalArgumentException());
		}
	}

	public static boolean isValidDatabase(String dbFilePath, String name) {
		String url = "jdbc:sqlite:" + dbFilePath;
		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn == null) {
				logError("Database; Failed to establish connection to: " + name);
				return false;
			}
			boolean hasLayoutTable = tableExists(dbFilePath, "layout");
			boolean hasDataTable = tableExists(dbFilePath, "data");
			logDebug("Database; [" + name + "] has layout table: " + hasLayoutTable + " has data table: "
					+ hasDataTable);
			boolean isValid = hasLayoutTable && hasDataTable;

			if (!isValid) {
				logWarn("Database; [" + name + "] Database missing required tables");
			}

			return isValid;
		} catch (SQLException e) {
			logError("Database; [" + name + "] SQL Exception while checking database: ");
			e.printStackTrace();
			return false;
		}
	}

	public static Map<String, String> getTableColumnsDefinition(String dbFilePath, String tableName)
			throws SQLException {
		String fullPath = dbFilePath.endsWith(".db") ? dbFilePath : dbFilePath + ".db";
		Map<String, String> columnsDefinition = new LinkedHashMap<>();

		String sql = "SELECT sql FROM sqlite_master WHERE type='table' AND name=?";
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + fullPath);
				PreparedStatement ps = conn.prepareStatement(sql)) {
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

	public static String getLayoutJsonFromDb(File dbFile) {
		if (dbFile == null || !dbFile.exists() || !dbFile.getName().endsWith(".db")) {
			logError("Invalid database file provided.");
			return null;
		}
		String dbPath = dbFile.getAbsolutePath().replaceFirst("[.][^.]+$", "");
		String dbName = dbFile.getName();
		if (!DynamicDB.isValidDatabase(dbFile.getAbsolutePath(), dbName)) {
			logError("The provided file is not a valid report database: " + dbName);
			return null;
		}
		Map<String, String> layoutSchema = new HashMap<>();
		layoutSchema.put("key", "TEXT");
		layoutSchema.put("layoutData", "TEXT");
		layoutSchema.put("transferData", "TEXT");
		DynamicDB databaseLayout = new DynamicDB(dbPath, "layout", "key", layoutSchema);
		if (!databaseLayout.initDB()) {
			logError("Failed to initialize layout database for: " + dbName);
			return null;
		}
		try {
			Map<String, Object> layoutMap = databaseLayout.getRecord("1");
			Map<String, Object> transferMap = databaseLayout.getRecord("2");
			String layoutData = (layoutMap != null && layoutMap.containsKey("layoutData"))
					? (String) layoutMap.get("layoutData")
					: null;
			String transferData = (transferMap != null && transferMap.containsKey("transferData"))
					? (String) transferMap.get("transferData")
					: null;
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode rootNode = mapper.createObjectNode();
			if (layoutData != null && !layoutData.isEmpty() && !layoutData.equals("null")) {
				try {
					JsonNode layoutJson = mapper.readTree(layoutData);
					rootNode.set("layout", layoutJson);
				} catch (JsonProcessingException e) {
					logError("Error parsing layoutData JSON for " + dbName, e);
				}
			}
			if (transferData != null && !transferData.isEmpty() && !transferData.equals("null")) {
				try {
					JsonNode transferJson = mapper.readTree(transferData);
					rootNode.set("transfer", transferJson);
				} catch (JsonProcessingException e) {
					logError("Error parsing transferData JSON for " + dbName, e);
				}
			}
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		} catch (SQLException | JsonProcessingException e) {
			logError("Error retrieving layout/transfer data from database: " + dbName, e);
			return null;
		} finally {
			try {
				databaseLayout.close();
			} catch (SQLException e) {
				logError("Error closing database connection for: " + dbName, e);
			}
		}
	}

	private static void parseColumnDefinitions(String createTableSql, Map<String, String> columnsDefinition) {
		int start = createTableSql.indexOf('(');
		int end = createTableSql.lastIndexOf(')');
		if (start == -1 || end == -1) {
			return;
		}

		String columnsPart = createTableSql.substring(start + 1, end).trim();
		String[] definitions = columnsPart.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

		for (String def : definitions) {
			def = def.trim();
			if (def.startsWith("PRIMARY KEY") || def.startsWith("FOREIGN KEY") || def.startsWith("CHECK")) {
				continue;
			}

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
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + fullPath);
				PreparedStatement ps = conn
						.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name=?")) {
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
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + fullPath);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(pragmaSql)) {

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

	public void addColumnIfNotExists(String columnName, String columnType) throws SQLException {
		boolean columnExists = false;
		String pragmaSql = "PRAGMA table_info(" + escapeIdentifier(tableName) + ")";
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(pragmaSql)) {
			while (rs.next()) {
				if (rs.getString("name").equalsIgnoreCase(columnName)) {
					columnExists = true;
					break;
				}
			}
		}

		if (!columnExists) {
			String sql = String.format("ALTER TABLE %s ADD COLUMN %s %s", escapeIdentifier(tableName),
					escapeIdentifier(columnName), columnType);

			try (Statement stmt = connection.createStatement()) {
				stmt.executeUpdate(sql);
				columnsDefinition.put(columnName, columnType);
				logInfo("Added column " + columnName + " to table " + tableName);
			}
		}
	}

	public boolean initDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
			logInfo("Connected to database: " + dbFilePath);
			createTableIfNotExists();
			return true;
		} catch (Exception e) {
			logError("Error connecting to database", e);
			return false;
		}
	}

	public boolean deleteRecord(String dbFilePath, String tableName, String primaryKeyColumn, Object pkValue) {
		String fullPath = dbFilePath.endsWith(".db") ? dbFilePath : dbFilePath + ".db";
		String url = "jdbc:sqlite:" + fullPath;
		try (Connection conn = DriverManager.getConnection(url)) {
			String sql = "DELETE FROM " + escapeIdentifier(tableName) + " WHERE " + escapeIdentifier(primaryKeyColumn)
					+ " = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setObject(1, pkValue);
				int affectedRows = ps.executeUpdate();
				if (affectedRows > 0) {
					logInfo("Deleted record with " + primaryKeyColumn + " = " + pkValue + " from " + tableName);
					return true;
				} else {
					logWarn("No record found with " + primaryKeyColumn + " = " + pkValue + " in " + tableName);
					return false;
				}
			}
		} catch (SQLException e) {
			logError("Error deleting record from " + tableName + " with " + primaryKeyColumn + " = " + pkValue, e);
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
			logInfo("Created table: " + tableName);
		} catch (SQLException e) {
			logError("Error creating table: " + e.getMessage(), e);
		}
	}

	public void addOrReplaceRecord(Map<String, Object> record) throws SQLException {
		validateRecord(record);
		Object pkValue = record.get(primaryKeyColumn);
		if (pkValue == null) {
			logError("Record must contain a value for primary key column: " + primaryKeyColumn,
					new IllegalArgumentException());
		}

		String checkSQL = "SELECT COUNT(*) FROM " + escapeIdentifier(tableName) + " WHERE "
				+ escapeIdentifier(primaryKeyColumn) + " = ?";

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
		logDebug("Record: " + record + " added or replaced in table: " + tableName);
	}

	// Replace the existing updateRecord method with this corrected version
	private void updateRecord(Map<String, Object> record) throws SQLException {
		StringBuilder setClause = new StringBuilder();
		List<Object> values = new ArrayList<>();

		// Iterate through the columns defined for the table
		for (String col : columnsDefinition.keySet()) {
			// Skip the primary key in the SET clause
			if (col.equals(primaryKeyColumn)) {
				continue;
			}

			// Check if the record map contains the column to be updated
			if (record.containsKey(col)) {
				if (setClause.length() > 0) {
					setClause.append(", ");
				}
				setClause.append(escapeIdentifier(col)).append(" = ?");
				values.add(record.get(col));
			}
		}

		// *** FIX: Check if there are any columns to update ***
		if (setClause.length() == 0) {
			logInfo("No updatable columns found in the record for primary key: " + record.get(primaryKeyColumn)
					+ ". Skipping update.");
			return; // Exit the method gracefully
		}

		// Add the primary key value for the WHERE clause
		values.add(record.get(primaryKeyColumn));

		String sql = "UPDATE " + escapeIdentifier(tableName) + " SET " + setClause.toString() + " WHERE "
				+ escapeIdentifier(primaryKeyColumn) + " = ?";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			for (int i = 0; i < values.size(); i++) {
				ps.setObject(i + 1, values.get(i));
			}
			int affected = ps.executeUpdate();
			if (affected == 0) {
				logWarn("Update statement affected 0 rows for primary key: " + record.get(primaryKeyColumn));
			}
		}
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
		String sql = "INSERT INTO " + escapeIdentifier(tableName) + " (" + columnsPart + ") VALUES (" + valuesPart
				+ ")";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			for (int i = 0; i < values.size(); i++) {
				ps.setObject(i + 1, values.get(i));
			}
			ps.executeUpdate();
		}
	}

	public Map<String, Object> getRecord(Object pkValue) throws SQLException {
		String sql = "SELECT * FROM " + escapeIdentifier(tableName) + " WHERE " + escapeIdentifier(primaryKeyColumn)
				+ " = ?";
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
						logError("Column '" + column + "' expects an INTEGER, but got "
								+ value.getClass().getSimpleName(), new IllegalArgumentException());
					}
					break;
				case "REAL":
					if (!(value instanceof Number)) {
						logError("Column '" + column + "' expects a REAL, but got " + value.getClass().getSimpleName(),
								new IllegalArgumentException());
					}
					break;
				case "TEXT":
					if (!(value instanceof String)) {
						logError("Column '" + column + "' expects TEXT, but got " + value.getClass().getSimpleName(),
								new IllegalArgumentException());
					}
					break;
				case "BLOB":
					if (!(value instanceof byte[])) {
						logError("Column '" + column + "' expects a BLOB (byte[]), but got "
								+ value.getClass().getSimpleName(), new IllegalArgumentException());
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
			logInfo("Database connection closed");
		}
	}

	public String getTableName() {
		return tableName;
	}
}
