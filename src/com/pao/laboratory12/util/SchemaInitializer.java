package com.pao.laboratory12.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class SchemaInitializer {

    private SchemaInitializer() {
    }

    public static void initFromSqliteScript(Connection conn) throws SQLException, IOException {
        String path = "/com/pao/laboratory12/resources/schema-sqlite.sql";
        try (InputStream is = SchemaInitializer.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Lipseste resource: " + path);
            }
            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            StringBuilder stmt = new StringBuilder();
            for (String line : sql.split("\n")) {
                String t = line.trim();
                if (t.startsWith("--") || t.isEmpty()) {
                    continue;
                }
                stmt.append(line).append('\n');
            }
            for (String part : stmt.toString().split(";")) {
                String s = part.trim();
                if (!s.isEmpty()) {
                    try (Statement st = conn.createStatement()) {
                        st.execute(s);
                    }
                }
            }
        }
    }
}
