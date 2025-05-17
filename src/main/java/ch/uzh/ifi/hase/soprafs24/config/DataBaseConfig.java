package ch.uzh.ifi.hase.soprafs24.config;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class DataBaseConfig {

    @Bean
    public DataSource dataSource() {
        String DB_PASSWORD = fetchDBPassword();

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://google/sopra_db?cloudSqlInstance=sopra-fs25-group-34-server:europe-west6:postgres-instance&socketFactory=com.google.cloud.sql.postgres.SocketFactory&user=postgres");
        dataSource.setUsername("postgres");
        dataSource.setPassword(DB_PASSWORD);
        return dataSource;
    }

    private String fetchDBPassword() {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            String resourceName = "projects/337244022712/secrets/DB_PASSWORD/versions/latest";
            AccessSecretVersionResponse response = client.accessSecretVersion(resourceName);
            /*
            response = {
                name: "projects/337244022712/secrets/DB_PASSWORD",
                payload: {
                    data: ByteString (token in bytes)
                }
            }
             */
            return response.getPayload().getData().toStringUtf8();
        }
        catch (Exception e) {
            System.err.println("Error fetching DB_PASSWORD using Secret Manager: " + e.getMessage());
            return null;
        }
    }
}
