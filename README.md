### Database Connection For JDBC

1. create Connection (myConn)
    ```java
    myConn = DriverManager.getConnection(connectUrl, user, pwd);
    ```
2. create PreparedStatement (prepStmt)
    ```java
    prepStmt = myConn.prepareStatement(sql);
    ```
3. sql-command use ? replace and set index as above example
    ```java
    prepStmt.setDouble(1, 80000);
    prepStmt.setString(2, "Legal");
    ```
4. as query to get ResultSet (myResult)
    ```java
    myResult = prepStmt.executeQuery();
    ```
5. as insert || update || delete int (affectedRows)
    ```java
    affectedRows = prepStmt.executeUpdate();
    ```
6. use myResult.next() to print results (metaData)

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