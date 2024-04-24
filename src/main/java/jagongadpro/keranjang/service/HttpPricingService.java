package jagongadpro.keranjang.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class HttpPricingService implements PricingService {
    private HttpClient client;
    private String apiUrl;

    public HttpPricingService(String apiUrl) {
        this.client = HttpClient.newHttpClient();
        this.apiUrl = apiUrl;
    }

    @Override
    public double getPrice(String id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + id))
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            // Asumsikan response body mengembalikan harga sebagai string
            return Double.parseDouble(response.body());
        } catch (Exception e) {
            System.err.println("Error while fetching price: " + e.getMessage());
            return 0.0; 
        }
    }
}
