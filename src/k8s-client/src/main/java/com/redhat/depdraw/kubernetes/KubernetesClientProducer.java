package com.redhat.depdraw.kubernetes;

import io.fabric8.kubernetes.client.*;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@Singleton
public class KubernetesClientProducer {

    @Produces
    public KubernetesClient kubernetesClient() {
        Config config = new ConfigBuilder()
                .withTrustCerts(false) // Don't blindly trust all certs
                .withCaCertFile("/Users/tmihalac/IdeaProjects/DepDraw/src/k8s-client/ca.crt") // Path to the CA certificate
                .withTrustStoreFile("/Users/tmihalac/IdeaProjects/DepDraw/src/k8s-client/k8s-truststore.jks") // Path to the keystore
                .withTrustStorePassphrase("changeit") // Keystore password
                .build();

        // here you would create a custom client
        return new KubernetesClientBuilder().withConfig(config).build();
    }
}
