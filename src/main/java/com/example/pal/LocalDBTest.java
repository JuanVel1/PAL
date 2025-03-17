package com.example.pal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LocalDBTest {
    public static void main(String[] args) {
        String url = "spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=SpringProject;encrypt=true;trustServerCertificate=true";

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("✅ Conexión exitosa a SQL LocalDB.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

