/*package com.studentwallet.config;
import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;

@Configuration
@EnableCassandraRepositories(basePackages = "com.studentwallet.repository")
public class AstraDbConfig {

    @Bean
    public CqlSession cqlSession() throws Exception {
        // Read environment variables
        String contactPoints = System.getenv("ASTRA_CONTACT_POINTS");
        String portStr = System.getenv("ASTRA_PORT");
        String datacenter = System.getenv("ASTRA_DATACENTER");
        String keyspace = System.getenv("ASTRA_KEYSPACE");
        String username = System.getenv("ASTRA_USERNAME");
        String password = System.getenv("ASTRA_TOKEN");

        // Fallback values (just in case env vars are missing locally)
        if (contactPoints == null || contactPoints.isEmpty()) {
            contactPoints = "6549e6b7-619e-4def-9029-65f82e5fb0c7-us-east-2.apps.astra.datastax.com";
        }
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("ASTRA_TOKEN environment variable not set! Please set it on Render.");
        }

        int port = (portStr != null) ? Integer.parseInt(portStr) : 29042;
        String dc = (datacenter != null) ? datacenter : "us-east-2";
        String ks = (keyspace != null) ? keyspace : "default_keyspace";
        String user = (username != null) ? username : "token";

        // Log the connection details (this will show in Render logs)
        System.out.println("==========================================");
        System.out.println("Connecting to Astra DB:");
        System.out.println("  Contact Points: " + contactPoints);
        System.out.println("  Port: " + port);
        System.out.println("  Datacenter: " + dc);
        System.out.println("  Keyspace: " + ks);
        System.out.println("==========================================");

        // Build the session with SSL explicitly enabled
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(contactPoints, port))
                .withLocalDatacenter(dc)
                .withKeyspace(ks)
                .withAuthCredentials(user, password)
                .withSslContext(SSLContext.getDefault()) // This enables SSL
                .build();
    }
}*/