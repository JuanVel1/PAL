package com.example.pal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LocalDBTest {
    public static void main(String[] args) {
        // URL de conexión a MySQL
        String url = "jdbc:mysql://buvnpkpkqpt4xsjwxq5i-mysql.services.clever-cloud.com:3306/buvnpkpkqpt4xsjwxq5i?useSSL=false&serverTimezone=UTC";
        String username = "uhdjjzwnbfd3e5b9";  // Reemplázalo con tu nombre de usuario de MySQL
        String password = "e8GBiskOkFoYlfowyEXg";  // Reemplázalo con tu contraseña de MySQL

        try {
            // Cargar el controlador de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                System.out.println("✅ Conexión exitosa a MySQL.");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el controlador JDBC: " + e.getMessage());
        }
    }
}
