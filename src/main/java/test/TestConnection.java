package test;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/devsync";
        String user = "devsync";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Connexion m3a la base de données mzyana!");
            } else {
                System.out.println("Connexion failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}