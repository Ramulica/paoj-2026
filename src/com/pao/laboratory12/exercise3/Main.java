package com.pao.laboratory12.exercise3;

import com.pao.laboratory12.util.DatabaseConnection;
import com.pao.laboratory12.util.SchemaInitializer;

/**
 * Bonus: comutare între dialecte prin {@code db.properties} + script SQL corespunzător.
 * Vezi Readme.md din exercise3 și template-ul {@code resources/db.properties.template}.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        var conn = DatabaseConnection.getInstance().getConnection();
        String url = conn.getMetaData().getURL();
        System.out.println("Conexiune activă: " + url);
        if (url.contains("sqlite")) {
            SchemaInitializer.initFromSqliteScript(conn);
            System.out.println("Schema SQLite reaplicată (DROP + CREATE).");
        } else {
            System.out.println("Pentru MySQL/H2 rulează manual schema indicată în Readme.");
        }
        DatabaseConnection.getInstance().close();
    }
}
