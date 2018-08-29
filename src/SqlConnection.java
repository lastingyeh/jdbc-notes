import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class SqlConnection {

	private String connectUrl;
	private String user;
	private String pwd;

	Connection myConn;
	Statement myStmt;
	PreparedStatement prepStmt;
	ResultSet myResult;
	CallableStatement callStmt;

	SqlConnection() throws SQLException, IOException {

		Properties properties = new Properties();
		properties.load(new FileInputStream("demo.properties"));

		connectUrl = properties.getProperty("dbUrl");
		user = properties.getProperty("user");
		pwd = properties.getProperty("pwd");

		myConn = DriverManager.getConnection(connectUrl, user, pwd);
		myStmt = myConn.createStatement();
	}

	public void executeQuery(String sql) throws SQLException {

		myResult = myStmt.executeQuery(sql);

		System.out.println("Query Result");

		display(myResult);

	}

	public void executeUpdate(String sql, String type) throws SQLException {

		// PreparedStatement prepStmt = myConn.prepareStatement(sql);

		// prepStmt.setInt(1, 1);

		// int rowsAffected = prepStmt.executeUpdate();

		int rowsAffected = myStmt.executeUpdate(sql);

		System.out.println(type + " Rows affected: " + rowsAffected);

	}

	public void executeQuery(String sql, boolean isPrepareStmt) throws SQLException {

		if (!isPrepareStmt) {
			System.out.println("Query Result");

			executeQuery(sql);
		} else {

			System.out.println("PreparedStatement Result");

			prepStmt = myConn.prepareStatement(sql);

			prepStmt.setDouble(1, 80000);
			prepStmt.setString(2, "Legal");

			myResult = prepStmt.executeQuery();

			display(myResult);
		}
	}

	public void execSP(String sp) throws SQLException {
		String dep = "Engineering";
		int amount = 10000;

		executeQuery("SELECT * FROM employees WHERE department = 'Engineering'");

		callStmt = myConn.prepareCall(String.format("{call %s(?, ?)}", sp));

		callStmt.setString(1, dep);
		callStmt.setInt(2, amount);

		callStmt.execute();

		executeQuery("SELECT * FROM employees WHERE department = 'Engineering'");
	}

	public void execSP_InOut(String sp) throws SQLException {
		String dep = "Engineering";

		callStmt = myConn.prepareCall(String.format("{call %s(?)}", sp));

		callStmt.setString(1, dep);

		callStmt.execute();

		String outResult = callStmt.getString(1);

		System.out.println("outResult: " + outResult);
	}

	public void execSP_Out(String sp) throws SQLException {
		String dep = "Engineering";

		callStmt = myConn.prepareCall(String.format("{call %s(?, ?)}", sp));

		callStmt.setString(1, dep);
		callStmt.registerOutParameter(2, Types.INTEGER);

		callStmt.execute();

		// get out result
		int depCount = callStmt.getInt(2);

		System.out.println("depCount: " + depCount);
	}

	public void execSP_ResultSet(String sp) throws SQLException {
		String dep = "Engineering";

		callStmt = myConn.prepareCall(String.format("{call %s(?)}", sp));

		callStmt.setString(1, dep);

		callStmt.execute();

		myResult = callStmt.getResultSet();

		display(myResult);
	}

	private void display(ResultSet myResult) throws SQLException {

		ResultSetMetaData metaData = myResult.getMetaData();
		int columnCount = metaData.getColumnCount();

		while (myResult.next()) {

			String printValue = "";

			for (int i = 1; i <= columnCount; i++) {
				printValue += myResult.getString(i) + ", ";
			}

			System.out.println(printValue + "\n");
		}
	}

	public void execTransaction() throws SQLException {
		myConn.setAutoCommit(false);

		System.out.println("Salaries BEFORE\n");
		showSalaries("HR");
		showSalaries("Engineering");

		// update actions
		executeUpdate("DELETE FROM employees WHERE department = 'HR'", "Delete");
		executeUpdate("UPDATE employees SET salary = 300000 WHERE department = 'Engineering'", "Update");

		boolean ok = askUserIfOkToSave();

		if (ok) {
			myConn.commit();
			System.out.println("\n>> Transaction COMMITTED.\n");
		} else {
			myConn.rollback();
			System.out.println("\n>> Transaction ROLLED BACK.\n");
		}

		System.out.println("Salaries AFTER\n");

		showSalaries("HR");
		showSalaries("Engineering");

	}

	private boolean askUserIfOkToSave() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Is is okay to save? yes/no: ");

		String input = scanner.nextLine();

		scanner.close();

		return input.equalsIgnoreCase("yes");
	}

	private void showSalaries(String dep) throws SQLException {

		prepStmt = myConn.prepareStatement("SELECT * FROM employees WHERE department = ?");

		prepStmt.setString(1, dep);

		myResult = prepStmt.executeQuery();

		display(myResult);
	}

	public void getDatabaseInfo() throws SQLException {
		DatabaseMetaData databaseMetaData = myConn.getMetaData();

		System.out.println("Product name: " + databaseMetaData.getDatabaseProductName());
		System.out.println("Product version: " + databaseMetaData.getDatabaseProductVersion());

		System.out.println();

		System.out.println("JDBC Driver name: " + databaseMetaData.getDriverName());
		System.out.println("JDBC Driver version: " + databaseMetaData.getDriverMajorVersion());

	}

	public void getTableInfo(String tableName) throws SQLException {
		DatabaseMetaData databaseMetaData = myConn.getMetaData();

		System.out.println("List of Tables");
		System.out.println("--------------");

		myResult = databaseMetaData.getTables(null, null, null, null);

		while (myResult.next()) {
			System.out.println(myResult.getString("TABLE_NAME"));
		}

		System.out.println("\n\nList of Columns");
		System.out.println("--------------");

		myResult = databaseMetaData.getColumns(null, null, tableName, null);

		while (myResult.next()) {
			System.out.println(myResult.getString("COLUMN_NAME"));
		}
	}

	public void getResultSetInfo() throws SQLException {
		myResult = myStmt.executeQuery("SELECT * FROM employees");

		ResultSetMetaData resultSetMetaData = myResult.getMetaData();

		int columnCount = resultSetMetaData.getColumnCount();
		System.out.println("Column count: " + columnCount + "\n");

		for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
			System.out.println("Column name: " + resultSetMetaData.getColumnName(colIndex));
			System.out.println("Column type name: " + resultSetMetaData.getColumnTypeName(colIndex));
			System.out.println("Is Nullable: " + resultSetMetaData.isNullable(colIndex));
			System.out.println("Is Auto Increment: " + resultSetMetaData.isAutoIncrement(colIndex) + "\n");
		}
	}

	public void updateAndWriteBlob(String sql) throws SQLException, IOException {

		FileInputStream inputStream = null;

		try {
			prepStmt = myConn.prepareStatement(sql);

			File file = new File("sample_resume.pdf");
			inputStream = new FileInputStream(file);

			// set input file
			prepStmt.setBinaryStream(1, inputStream);

			System.out.println("\nStoring resume in database: " + file);
			System.out.println(sql);

			prepStmt.executeUpdate();

			System.out.println("\nCompleted successfully!");

		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	public void readAndSaveBlob(String sql, String email) throws SQLException, IOException {

		FileOutputStream outputStream = null;
		InputStream inputStream = null;

		try {
			prepStmt = myConn.prepareStatement(sql);

			prepStmt.setString(1, email);

			myResult = prepStmt.executeQuery();

			// create file as saved
			File outFile = new File("resume_from_db.pdf");
			outputStream = new FileOutputStream(outFile);

			if (myResult.next()) {
				inputStream = myResult.getBinaryStream("resume");

				System.out.println("Reading resume from database...");
				System.out.println(sql);

				byte[] buffer = new byte[1024];

				while (inputStream.read(buffer) > 0) {
					outputStream.write(buffer);
				}

				System.out.println("\nSaved to file: " + outFile.getAbsolutePath());
				System.out.println("\nCompleted successfully!");
			}

		} finally {

			if (inputStream != null) {
				inputStream.close();
			}

			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public void updateAndWriteClob(String sql, String email) throws SQLException, IOException {

		FileReader fileReader = null;

		try {
			prepStmt = myConn.prepareStatement(sql);

			File file = new File("sample_resume.txt");
			fileReader = new FileReader(file);

			prepStmt.setCharacterStream(1, fileReader);
			prepStmt.setString(2, email);

			System.out.println("Reading input file: " + file.getAbsolutePath());

			System.out.println("\nStoring resume in database: " + file);
			System.out.println(sql);

			prepStmt.executeUpdate();

			System.out.println("\nCompleted successfully!");

		} finally {
			if (fileReader != null) {
				fileReader.close();
			}
		}
	}

	public void readAndSaveClob(String sql, String email) throws SQLException, IOException {

		FileWriter fileWriter = null;
		Reader reader = null;

		try {
			prepStmt = myConn.prepareStatement(sql);

			prepStmt.setString(1, email);

			myResult = prepStmt.executeQuery();

			File file = new File("resume_from_db.txt");

			fileWriter = new FileWriter(file);

			if (myResult.next()) {
				reader = myResult.getCharacterStream("resume");

				System.out.println("Reading resume from database...");
				System.out.println(sql);

				int schar;

				while ((schar = reader.read()) > 0) {
					fileWriter.write(schar);
				}

				System.out.println("\nSaved to file: " + file.getAbsolutePath());
				System.out.println("\nCompleted successfully!");
			}
		} finally {
			if (fileWriter != null) {
				fileWriter.close();
			}

			if (reader != null) {
				reader.close();
			}
		}
	}

	public void closeConnection() throws SQLException {

		if (myResult != null) {
			myResult.close();
		}

		if (myStmt != null) {
			myStmt.close();
		}

		if (prepStmt != null) {
			prepStmt.close();
		}

		if (myConn != null) {
			myConn.close();
		}

		if (callStmt != null) {
			callStmt.close();
		}
	}
}