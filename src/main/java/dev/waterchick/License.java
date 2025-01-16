package dev.waterchick;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class License {

    private final String pluginId;
    private final String licenseId;
    private final static String LICENSE_HOST = "https://license.waterchick.dev/licenses/validate";

    public License(String pluginId, String licenseId) {
        this.pluginId = pluginId;
        this.licenseId = licenseId;
    }

    public boolean validate() {
        if (Thread.currentThread().getName().equals("main")) {
            throw new IllegalStateException("This method can only be called asynchronously.");
        }

        // JSON payload
        String jsonInputString = String.format("{\"licenseId\": \"%s\", \"pluginId\": \"%s\"}", licenseId, pluginId);

        try {
            // Vytvoření HttpClientu
            HttpClient client = HttpClient.newHttpClient();

            // Vytvoření požadavku
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(LICENSE_HOST))
                    .header("Content-Type", "application/json; utf-8")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString, StandardCharsets.UTF_8))
                    .build();

            // Odeslání požadavku a zpracování odpovědi
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Kontrola odpovědi
            return response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}