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

#### CreateStatement

1. Get Statement 
    ```java
    myStmt = myConn.createStatement();
    ```
2. Exec query by statement
    ```java
    ResultSet myResult = myStmt.executeQuery(sql);
    ```
3. Insert || Update || Delete
    ```java
    int affectedRows = myStmt.executeUpdate(sql);
    ```
#### PreparedStatement

1. Get PreparedStatement
    ```java
    PreparedStatement prepStmt = myConn.prepareStatement(sql);
    ```
2. Set SQL params ex. sql = "SELECT * FROM employees WHERE salary > ? AND department = ?";

    ```java
    prepStmt.setDouble(1, 80000);

    prepStmt.setString(2, "Legal");
    ```
3. as query to get ResultSet (myResult)
    ```java
    ResultSet myResult = prepStmt.executeQuery();
    ```
4. as insert || update || delete int (affectedRows)
    ```java
    int affectedRows = prepStmt.executeUpdate();
    ```
#### Get ResultSet

1. use myResult.next() to print results (metaData)

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

### Reference And Thanks for

[Udemy Course - Java Database Connection: JDBC and MySQL](https://www.udemy.com/how-to-connect-java-jdbc-to-mysql/)