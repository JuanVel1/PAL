package com.example.pal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SupabaseDBTest {
    public static void main(String[] args) {
        // URL JDBC para Supabase
        // Reemplaza con las variables de entorno
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String url = System.getenv("DB_URL");

        System.out.println("Connecting to database...");
        System.out.println(username);
        System.out.println(password);

        try {
            // Cargar el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Intentar conexión
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                System.out.println("✅ Conexión exitosa a Supabase PostgreSQL.");
            } catch (SQLException e) {
                System.out.println("❌ Error en la conexión a Supabase: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error al cargar el driver JDBC: " + e.getMessage());
        }
    }
}
