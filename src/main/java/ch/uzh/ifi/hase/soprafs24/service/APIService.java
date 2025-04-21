package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class APIService {

    private final String DEVELOPER_TOKEN;
    private final String API_URL;
    private final String storefront = "ch";

    private final WebClient webClient;

    @Autowired
    public APIService(WebClient.Builder webClientBuilder) {
        this.API_URL = "https://api.music.apple.com/v1";
        this.DEVELOPER_TOKEN = getToken();
        this.webClient = webClientBuilder.baseUrl(this.API_URL).defaultHeader("Authorization", "Bearer " + this.DEVELOPER_TOKEN).build(); //DEVELOPER_TOKEN is by default the header.

    }

    //for test
    public APIService(WebClient.Builder webClientBuilder, String token) {
        this.API_URL = "https://api.music.apple.com/v1";
        this.DEVELOPER_TOKEN = token;
        this.webClient = webClientBuilder.baseUrl(this.API_URL).defaultHeader("Authorization", "Bearer " + this.DEVELOPER_TOKEN).build(); //DEVELOPER_TOKEN is by default the header.

    }

    String getToken() {
        //Try with env variable (for locally running)
        String envToken = System.getenv("DEVELOPER_TOKEN");
        if (envToken != null && !envToken.isEmpty()) {return envToken;}
        //Get Token from secret manager google cloud
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            String resourceName = "projects/337244022712/secrets/DEVELOPER_TOKEN";
            AccessSecretVersionResponse response = client.accessSecretVersion(resourceName);
            /*
            response = {
                name: "projects/337244022712/secrets/DEVELOPER_TOKEN",
                payload: {
                    data: ByteString (token in bytes)
                }
            }
             */


            return response.getPayload().getData().toStringUtf8();
        }
        catch (Exception e) {
            throw new RuntimeException("DEVELOPER_TOKEN not found in SecretManager ", e);
        }
    }

    //get catalog song detail
    //GET https://api.music.apple.com/v1/catalog/{storefront}/songs/{id}
    public SongCard getSongById(String id) {
        JsonNode APISong = webClient.get()
                .uri("/catalog/" + this.storefront + "/songs/" + id)
                .retrieve()
                /*.onStatus(HttpStatus::isError, response -> switch (response.rawStatusCode()){
                    case 401 -> Mono.error(new Exception("Unauthorized Apple Music API call"));
                    case 500 -> Mono.error(new Exception("Internal Server Error (Apple Music API call)"));
                    default ->  Mono.error(new Exception("Something went wrong during Apple Music API call"));
                }*/
                .bodyToMono(JsonNode.class)
                .block();
        /*
        APISong
            └──"data" :ArrayNode
                └── [0] :ObjectNode
                    ├──"id" :"1613600188"
                    ├──"type" :"songs"
                    ├──"attributes" :ObjectNode
                    │     ├──"name" :"Entropy"
                    │     ├──"artistName" :"Beach Bunny"
                    │     ├──"releaseDate" :"2022-06-09"
                    │     ├──"albumName" :"Emotional Creature"
                    │     └──"previews" :ArrayNode
                    │           └── [0] :ObjectNode
                    │                 └──"url" :"..."
                    └── ...
        */
        SongCard songCard = new SongCard();
        String title = APISong.get("data").get(0).get("attributes").get("name").asText();
        String artist = APISong.get("data").get(0).get("attributes").get("artistName").asText();
        String releaseDate = APISong.get("data").get(0).get("attributes").get("releaseDate").asText(); //"YYYY-MM-DD
        String previewURL = APISong.get("data").get(0).get("attributes").get("previews").get(0).get("url").asText();
        title = title.split("\\(")[0];
        songCard.setTitle(title);
        songCard.setArtist(artist);
        songCard.setYear(Integer.parseInt(releaseDate.substring(0,4))); //only first 4 i.e the year
        songCard.setSongURL(previewURL);

        return songCard;
    }

    //get playlist returns only list[Id]
    //GET https://api.music.apple.com/v1/catalog/{storefront}/playlists/{id}
    public List<String> getSongsOfPlaylist(String id) {
        JsonNode APIPlaylists = webClient.get()
                .uri("/catalog/" + this.storefront + "/playlists/" + id)
                .retrieve()
                /*.onStatus(HttpStatus::isError, response -> switch (response.rawStatusCode()) {
                    case 401 -> Mono.error(new Exception("Unauthorized Apple Music API call"));
                    case 500 -> Mono.error(new Exception("Internal Server Error (Apple Music API call)"));
                    default -> Mono.error(new Exception("Something went wrong during Apple Music API call"));
                }*/
                .bodyToMono(JsonNode.class)
                .block();
        List<String> songIds = new ArrayList<>();
        JsonNode tracks = APIPlaylists.get("data").get(0).get("relationships").get("tracks").get("data");
        /*
            │
        "tracks"
            └──"data" :ArrayNode
                ├── [0] :ObjectNode
                │   ├──"id" :"1615585008"
                │   └── ...
                ├── [1] :ObjectNode
                │   ├──"id" :"1556175854"
                │   └── ...
                ├── [2] :ObjectNode
                │   ├──"id" :"1606052735"
                │   └── ...
                └── ...
         */
         for (JsonNode song : tracks) {
            songIds.add(song.get("id").asText());
         }

         return songIds;
    }




}
