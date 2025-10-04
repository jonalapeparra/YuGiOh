package com.example.ygoduel.api;

import com.example.ygoduel.model.Carta;
import org.json.JSONObject;
import org.json.JSONArray;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
/**
 * Esta es la clase cliente que consume la API pública de YGOPRODECK.
 * Se conecta al endpoint de cartas y obtiene una carta Monster de forma aleatoria.
 */

public class YgoApiClient {
    // URL que devuelve solo cartas tipo "Normal Monster"
    private static final String MONSTER_URL = "https://db.ygoprodeck.com/api/v7/cardinfo.php?type=Normal%20Monster";
    // Cliente HTTP de Java 11 configurado para seguir redirecciones
    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();
    private final Random random = new Random();
    /**
     * Devuelve una carta Monster aleatoria desde la API.
     */
    public Carta fetchRandomMonsterCard(int i) throws Exception {
        // Hacemos la petición HTTP GET y pedir todas las cartas tipo Monster (aquí solo tipo Monster)
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(MONSTER_URL))
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new Exception("HTTP error: " + resp.statusCode());
        }
        //  Parseamos el JSON usando org.json
        JSONObject obj = new JSONObject(resp.body());
        JSONArray data = obj.getJSONArray("data");

        // escoger una al azar
        int idx = random.nextInt(data.length());
        JSONObject cardObj = data.getJSONObject(idx);

        // Obtenemos los campos principales de la carta
        String name = cardObj.optString("name", "Unknown");
        int atk = cardObj.optInt("atk", 0);
        int def = cardObj.optInt("def", 0);

        // Extraemos la URL de la imagen
        String imageUrl = null;
        if (cardObj.has("card_images")) {
            JSONArray imgs = cardObj.getJSONArray("card_images");
            if (!imgs.isEmpty()) {
                imageUrl = imgs.getJSONObject(0).optString("image_url", null);
            }
        }

        // Creamos y devolvemos la carta
        return new Carta(name, atk, def, imageUrl);
    }
}
