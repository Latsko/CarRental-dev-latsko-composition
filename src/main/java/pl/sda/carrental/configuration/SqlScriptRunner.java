package pl.sda.carrental.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;

@Component
public class SqlScriptRunner implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        String sqlScriptPath = "src/main/resources/data.sql";

        try (BufferedReader reader = new BufferedReader(new FileReader(sqlScriptPath))) {
            StringBuilder sqlStatementBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    sqlStatementBuilder.append(line.trim());
                    if (line.endsWith(";")) {
                        String sqlStatement = sqlStatementBuilder.toString();
                        jdbcTemplate.execute(sqlStatement);
                        sqlStatementBuilder.setLength(0);
                    }
                }
            }
        }
    }
}
