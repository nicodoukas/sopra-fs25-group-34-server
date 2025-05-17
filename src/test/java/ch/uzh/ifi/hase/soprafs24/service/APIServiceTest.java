package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;


import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class APIServiceTest {
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;


    private APIService apiService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        String mockToken = "mockDeveloperToken";

        //WebClient mock
        Mockito.when(webClientBuilder.baseUrl("https://api.music.apple.com/v1")).thenReturn(webClientBuilder);
        Mockito.when(webClientBuilder.defaultHeader("Authorization", "Bearer " + mockToken)).thenReturn(webClientBuilder);
        Mockito.when(webClientBuilder.build()).thenReturn(webClientMock);
        Mockito.when(responseSpecMock.onStatus(Mockito.any(), Mockito.any())).thenReturn(responseSpecMock);

        //URI & responseSpecMock
        Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersUriSpecMock.uri(Mockito.anyString(), Mockito.<Object[]>any())).thenReturn(requestHeadersMock);
        Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseSpecMock);
        Mockito.when(responseSpecMock.onStatus(Mockito.any(), Mockito.any())).thenReturn(responseSpecMock);


        apiService = new APIService(webClientBuilder, mockToken);
    }

    @Test
    public void getSongById_success() {
        String songId = "1613600188";
        String jsonMock = """
                {
                    "data": [
                        {
                            "id": "1613600188",
                            "type": "songs",
                            "href": "/v1/catalog/us/songs/1613600188",
                            "attributes": {
                                "albumName": "Emotional Creature",
                                "genreNames": [
                                    "Alternative",
                                    "Music"
                                ],
                                "trackNumber": 1,
                                "durationInMillis": 221000,
                                "releaseDate": "2022-06-09",
                                "isrc": "USQE92100257",
                                "artwork": {
                                    "width": 3000,
                                    "height": 3000,
                                    "url": "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/df/4e/68/df4e6833-9828-51d7-cdeb-71ecf6d3a23d/810090090962.png/{w}x{h}bb.jpg",
                                    "bgColor": "202020",
                                    "textColor1": "aea6f6",
                                    "textColor2": "b68ef6",
                                    "textColor3": "918bcb",
                                    "textColor4": "9878cb"
                                },
                                "composerName": "Anthony Vaccaro, Jon Alvarado, Lili Trifilio & Matt Henkels",
                                "playParams": {
                                    "id": "1613600188",
                                    "kind": "song"
                                },
                                "url": "https://music.apple.com/us/album/entropy/1613600183?i=1613600188",
                                "discNumber": 1,
                                "hasLyrics": true,
                                "isAppleDigitalMaster": true,
                                "name": "Entropy",
                                "previews": [
                                    {
                                        "url": "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview122/v4/72/a3/ab/72a3ab79-0066-f773-6618-7a53adc250b3/mzaf_17921540907592750976.plus.aac.p.m4a"
                                    }
                                ],
                                "artistName": "Beach Bunny"
                            },
                            "relationships": {
                                "artists": {
                                    "href": "/v1/catalog/us/songs/1613600188/artists",
                                    "data": [
                                        {
                                            "id": "1147783278",
                                            "type": "artists",
                                            "href": "/v1/catalog/us/artists/1147783278"
                                        }
                                    ]
                                },
                                "albums": {
                                    "href": "/v1/catalog/us/songs/1613600188/albums",
                                    "data": [
                                        {
                                            "id": "1613600183",
                                            "type": "albums",
                                            "href": "/v1/catalog/us/albums/1613600183"
                                        }
                                    ]
                                }
                            }
                        }
                    ]
                }
                """;
        JsonNode response;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.readTree(jsonMock);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Mockito.when(responseSpecMock.bodyToMono(JsonNode.class)).thenReturn(Mono.just(response));

        SongCard songCard = apiService.getSongById(songId);

        assertEquals("Entropy", songCard.getTitle());
        assertEquals("Beach Bunny", songCard.getArtist());
        assertEquals(2022, songCard.getYear());
        assertEquals("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview122/v4/72/a3/ab/72a3ab79-0066-f773-6618-7a53adc250b3/mzaf_17921540907592750976.plus.aac.p.m4a", songCard.getSongURL());
    }

    @Test
    public void getPlaylistById_success() {
        String playlistId = "pl.f4d106fed2bd41149aaacabb233eb5eb";
        String jsonMock = """
                {
                  "data": [
                    {
                      "id": "pl.f4d106fed2bd41149aaacabb233eb5eb",
                      "type": "playlists",
                      "href": "/v1/catalog/us/playlists/pl.f4d106fed2bd41149aaacabb233eb5eb",
                      "attributes": {
                        "artwork": {
                          "width": 1080,
                          "height": 1080,
                          "url": "https://is1-ssl.mzstatic.com/image/thumb/Features122/v4/cf/a6/0b/cfa60b1a-2801-d01f-43ca-32cf75b3bd52/7d2ff641-e31b-48ae-9ba8-1d95435405cf.png/{w}x{h}SC.DN01.jpg?l=en-US",
                          "bgColor": "f4f4f4",
                          "textColor1": "000000",
                          "textColor2": "232121",
                          "textColor3": "473e3b",
                          "textColor4": "444243"
                        },
                        "isChart": false,
                        "url": "https://music.apple.com/us/playlist/todays-hits/pl.f4d106fed2bd41149aaacabb233eb5eb",
                        "lastModifiedDate": "2022-04-13T19:01:51Z",
                        "name": "Today’s Hits",
                        "playlistType": "editorial",
                        "curatorName": "Apple Music Hits",
                        "playParams": {
                          "id": "pl.f4d106fed2bd41149aaacabb233eb5eb",
                          "kind": "playlist",
                          "versionHash": "993793b38a53ec95717db443e32a4f9f984ee4c7b8ca88494fa33f126a8ca317"
                        },
                        "description": {
                          "standard": "The first taste of Harry Styles’ forthcoming third album, <I>Harry’s House</I>, has arrived. On “As It Was,” a brisk, daydreamy ballad co-written with Kid Harpoon and Tyler Johnson, the boy-band icon turned bona fide rock star laments one of life’s painful dichotomies: the temptation to look back while being forced to move on ahead. “You know it’s not the same as it was,” he sings on the hook. “In this world, it’s just us.” Add Today’s Hits to your library to stay up on the biggest songs in pop, hip-hop, R&B, and more.",
                          "short": "“As It Was,” the daydreamy first single from Harry’s new album, has arrived—in Spatial."
                        }
                      },
                      "relationships": {
                        "tracks": {
                          "href": "/v1/catalog/us/playlists/pl.f4d106fed2bd41149aaacabb233eb5eb/tracks",
                          "data": [
                            {
                              "id": "1615585008",
                              "type": "songs",
                              "href": "/v1/catalog/us/songs/1615585008",
                              "attributes": {
                                "previews": [
                                  {
                                    "url": "https://audio-ssl.itunes.apple.com/itunes-itms8-assets/AudioPreview112/v4/ed/6b/40/ed6b4050-43a0-7f4d-759a-1575c1ab5022/mzaf_4656295327266779662.plus.aac.p.m4a"
                                  }
                                ],
                                "artwork": {
                                  "width": 3000,
                                  "height": 3000,
                                  "url": "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/2a/19/fb/2a19fb85-2f70-9e44-f2a9-82abe679b88e/886449990061.jpg/{w}x{h}bb.jpg",
                                  "bgColor": "d2c8ad",
                                  "textColor1": "090f12",
                                  "textColor2": "3d2b16",
                                  "textColor3": "313431",
                                  "textColor4": "5b4a34"
                                },
                                "artistName": "Harry Styles",
                                "url": "https://music.apple.com/us/album/as-it-was/1615584999?i=1615585008",
                                "discNumber": 1,
                                "genreNames": [
                                  "Pop",
                                  "Music"
                                ],
                                "durationInMillis": 167303,
                                "releaseDate": "2022-03-31",
                                "isAppleDigitalMaster": false,
                                "name": "As It Was",
                                "isrc": "USSM12200612",
                                "hasLyrics": true,
                                "albumName": "Harry's House",
                                "playParams": {
                                  "id": "1615585008",
                                  "kind": "song"
                                },
                                "trackNumber": 4,
                                "composerName": "Harry Styles, Kid Harpoon & Tyler Johnson"
                              }
                            },
                            {
                              "id": "1556175854",
                              "type": "songs",
                              "href": "/v1/catalog/us/songs/1556175854",
                              "attributes": {
                              "previews": [
                                {
                                  "url": "https://audio-ssl.itunes.apple.com/itunes-itms8-assets/AudioPreview116/v4/f1/c6/4d/f1c64de7-f813-6d3a-abf3-d980882070ab/mzaf_12341366234979933796.plus.aac.p.m4a"
                                }
                                ],
                              "artwork": {
                                "width": 3000,
                                "height": 3000,
                                "url": "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/f5/7a/9e/f57a9e6a-31c8-0784-dfbd-4a0120bfd4af/21UMGIM17517.rgb.jpg/{w}x{h}bb.jpg",
                                "bgColor": "1b190d",
                                "textColor1": "97e8d9",
                                "textColor2": "7be9cf",
                                "textColor3": "7ebfb0",
                                "textColor4": "68c0a8"
                                },
                              "artistName": "Justin Bieber",
                              "url": "https://music.apple.com/us/album/ghost/1556175419?i=1556175854",
                              "discNumber": 1,
                              "genreNames": [
                                "Pop",
                                "Music"
                                ],
                              "durationInMillis": 153190,
                              "releaseDate": "2021-03-19",
                              "isAppleDigitalMaster": true,
                              "name": "Ghost",
                              "isrc": "USUM72102635",
                              "hasLyrics": true,
                              "albumName": "Justice",
                              "playParams": {
                                "id": "1556175854",
                                "kind": "song"
                              },
                              "trackNumber": 11,
                              "composerName": "Justin Bieber, Jonathan Bellion, Jordan K. Johnson, Stefan Johnson & Michael Pollack"
                              }
                            }
                          ]
                        },
                        "curator": {
                          "href": "/v1/catalog/us/playlists/pl.f4d106fed2bd41149aaacabb233eb5eb/curator",
                          "data": [
                            {
                              "id": "1526756058",
                              "type": "apple-curators",
                              "href": "/v1/catalog/us/apple-curators/1526756058"
                            }
                          ]
                        }
                      }
                    }
                  ]
                }
                """;
        JsonNode response;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.readTree(jsonMock);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Mockito.when(responseSpecMock.bodyToMono(JsonNode.class)).thenReturn(Mono.just(response));

        List<String> songs = apiService.getSongsOfPlaylist(playlistId);

        assertEquals(2, songs.size());
        assertEquals("1615585008", songs.get(0));
        assertEquals("1556175854", songs.get(1));


    }
}
