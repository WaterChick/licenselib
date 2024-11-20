package dev.waterchick;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class License {

    private final String pluginId;
    private final String licenseId;
    private final static String LICENSE_HOST = "http://89.203.248.250:8080/licenses/validate";

    public License(String pluginId, String licenseId) throws IOException {
        this.pluginId = pluginId;
        this.licenseId = licenseId;
    }

    public boolean validate() {
        if (Thread.currentThread().getName().equals("main")) {
            throw new IllegalStateException("This method can only be called asynchronously.");
        }

        // Odeslat požadavek na backend
        String jsonInputString = String.format("{\"licenseId\": \"%s\", \"pluginId\": \"%s\"}", licenseId, pluginId); // JSON payload

        try {
            // Vytvoření URL a spojení
            URL url = URI.create(LICENSE_HOST).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Odeslání požadavku
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Získání kódu odpovědi
            int responseCode = connection.getResponseCode();

            // Porovnání kódu odpovědi a vrácení boolean
            return responseCode == HttpURLConnection.HTTP_OK; // Kontroluje kód 202 (Accepted)
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Vrátí false v případě chyby
        }
    }



}