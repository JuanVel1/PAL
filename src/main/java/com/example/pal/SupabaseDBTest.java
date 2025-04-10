package com.example.pal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SupabaseDBTest {
    public static void main(String[] args) {
        // URL JDBC para Supabase
        String username = "neondb_owner";  // Reemplázalo con el usuario correcto de Supabase
        String password = "npg_xhZ5LHtw4GnE";  // Reemplázalo con la contraseña correcta
        String url = "jdbc:postgresql://ep-icy-hall-a5vp89l1-pooler.us-east-2.aws.neon.tech/neondb?sslmode=require";



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
