### Database Connection For JDBC

#### Create Database Connection

1. download driver 

* [MsSQL](https://docs.microsoft.com/zh-tw/sql/connect/jdbc/using-the-jdbc-driver?view=sql-server-2017)

* [MySQL](https://dev.mysql.com/downloads/connector/j/5.1.html)

2. set connectionString

* MsSQL: jdbc:sqlserver://localhost:port;database=db

* MySQL: jdbc:mysql://localhost:3306/demo?useSSL=true

3. create Connection instance

```java
Connection myConn = DriverManager.getConnection(connectionString, user, pwd);
```

CreateStatement

```java
Statement myStmt = myConn.createStatement();

// Select Command
ResultSet myResult = myStmt.executeQuery(sql);

// Insert | Update | Delete
int affectedRows = myStmt.executeUpdate(sql);
```
PreparedStatement

```java
PreparedStatement prepStmt = myConn.prepareStatement(sql);

// Set SQL params 
// String sql = "SELECT * FROM employees WHERE salary > ? AND department = ?";

prepStmt.setDouble(1, 80000);
prepStmt.setString(2, "Legal");

// Select Command
ResultSet myResult = prepStmt.executeQuery();

// Insert | Update | Delete
int affectedRows = prepStmt.executeUpdate();
```

ResultSet

```java
ResultSetMetaData metaData = myResult.getMetaData();

int columnCount = metaData.getColumnCount();

while (myResult.next()) {

    String printValue = "";

    for (int i = 1; i <= columnCount; i++) {
        printValue += myResult.getString(i) + ", ";
    }

    System.out.println(printValue + "\n");
}
```

### Reference

[Udemy Course - Java Database Connection: JDBC and MySQL](https://www.udemy.com/how-to-connect-java-jdbc-to-mysql/)