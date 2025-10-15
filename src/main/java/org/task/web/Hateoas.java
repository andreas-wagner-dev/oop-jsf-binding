package org.task.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Hateoas{
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {
        URI apiRoot = new URI("http://localhost:8080/api");
        // Erster Schritt: Abfrage des Root-Endpunkts
        JsonNode rootResponse = getJsonResponse(apiRoot);

        // Nächster Schritt: Suche nach dem Link zur Benutzerliste
        String usersLink = rootResponse.at("/_links/users/href").asText();
        System.out.println("Gefundener Link zur Benutzerliste: " + usersLink);

        // Abfrage der Benutzerliste
        JsonNode usersResponse = getJsonResponse(new URI(usersLink));
        JsonNode firstUser = usersResponse.at("/_embedded/users/0");
        String firstUserSelfLink = firstUser.at("/_links/self/href").asText();
        System.out.println("Gefundener Link für den ersten Benutzer: " + firstUserSelfLink);

        // Abfrage des ersten Benutzers
        JsonNode userResponse = getJsonResponse(new URI(firstUserSelfLink));
        System.out.println("Benutzer-Name: " + userResponse.at("/name").asText());

        // Suche nach dem Lösch-Link
        String deleteLink = userResponse.at("/_links/delete/href").asText();
        if (!deleteLink.isEmpty()) {
            System.out.println("Lösch-Aktion verfügbar unter: " + deleteLink);
            // Hier würde der Client die DELETE-Anfrage senden
        } else {
            System.out.println("Löschen ist nicht erlaubt.");
        }
    }

    private static JsonNode getJsonResponse(URI uri) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/hal+json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readTree(response.body());
    }
}
