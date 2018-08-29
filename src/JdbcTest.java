
public class JdbcTest {

	static SqlConnection connection;

	public static void main(String[] args) {

		try {
			connection = new SqlConnection();

			// // insert
			// connection.executeUpdate(
			// "INSERT INTO employees (last_name, first_name, email, department, salary)
			// VALUES ('Wright', 'Eric', 'eric.wright@foo.com', 'HR', 33000.00)",
			// "Insert");

			// // update
			// connection.executeUpdate(
			// "UPDATE employees SET email='john@updatecode.com' WHERE last_name='Doe' and
			// first_name='John'",
			// "update");

			// // delete
			// connection.executeUpdate("DELETE FROM employees WHERE last_name='Wright' and
			// first_name='Eric'", "Delete");

			// // select
			// connection.executeQuery("SELECT * FROM employees");

			// // select by prepare
			// connection.executeQuery("SELECT * FROM employees WHERE salary > ? AND
			// department = ?", true);

			// call sp
			// connection.executeStoreProcedure("increase_salaries_for_department");

			// call sp for InOut
			// connection.execSP_InOut("greet_the_department");

			// call sp for Out
			// connection.execSP_Out("get_count_for_department");

			// call sp for resultSet
			// connection.execSP_ResultSet("get_employees_for_department");

			// call transaction
			// connection.execTransaction();
			// connection.getDatabaseInfo();

			// connection.getTableInfo("employees");

			// connection.getResultSetInfo();
			// connection.updateAndWriteBlob("UPDATE employees SET resume = ? WHERE
			// email = 'john.doe@foo.com'");
			// connection.readAndSaveBlob("SELECT resume FROM employees WHERE email = ?",
			// "john.doe@foo.com");
			// connection.updateAndWriteClob("UPDATE employees SET resume = ? WHERE email =
			// ? ", "john.doe@foo.com");
			connection.readAndSaveClob("SELECT resume FROM employees WHERE email = ?", "john.doe@foo.com");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				connection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
