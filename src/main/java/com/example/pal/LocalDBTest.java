package com.example.pal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LocalDBTest {
    public static void main(String[] args) {
        // URL JDBC para Supabase
        String url = "jdbc:postgresql://db.cagbdawizukjxdlhzltb.supabase.co:5432/postgres";
        String username = "postgres";  // Reemplázalo con el usuario correcto de Supabase
        String password = "zTmRx4w7OUqk0czxGin8";  // Reemplázalo con la contraseña correcta

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
