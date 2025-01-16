package dev.waterchick;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
            // Vytvoření klienta
            HttpClient client = HttpClient.newHttpClient();

            // Vytvoření požadavku
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(LICENSE_HOST))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            // Odeslání požadavku a získání odpovědi
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Debug výpis
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            // Vrácení výsledku
            return response.statusCode() == 200; // Kontroluje kód 200 (OK)
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}