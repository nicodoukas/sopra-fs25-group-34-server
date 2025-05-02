package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.storage.GameStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@Service
@Transactional
public class GameService {

    // TODO: at the moment we log nothing here - remove logger or actually log activities?
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameStorage gameStorage;
    // TODO: we also do not need the userRepository, remove if we wont need in future
    private final UserRepository userRepository;
    private final APIHandler apiHandler;
    private final LobbyService lobbyService;


    @Autowired
    public GameService(GameStorage gameStorage, UserRepository userRepository, LobbyService lobbyService, APIHandler apiHandler) {
        this.gameStorage = gameStorage;
        this.userRepository = userRepository;
        this.apiHandler = apiHandler;
        this.lobbyService = lobbyService;
    }

    public Game getGameById(Long gameId){
        return gameStorage.getGameById(gameId);
    }

    public Player getPlayerInGame(Long gameId, Long userId) {
        Game game = gameStorage.getGameById(gameId);

        return game.getPlayers()
                .stream()
                .filter(player -> player.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player " + userId + " not found in game " + gameId));
    }

    public SongCard getSongCard(Long gameId) {
        Game game = getGameById(gameId);
        return game.getCurrentRound().getSongCard(); //get SongCard from currentRound
    }

    public Player insertSongCardIntoTimeline(Long gameId, Long userId, SongCard songCard, int position) {
        Game game = getGameById(gameId);

        Player player = game.getPlayers()
                .stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player " + userId + " not found in game " + gameId));

        // Player already has updateTimeline function
        player.updateTimeline(position, songCard);
        return player;
    }

    public Player addCoinToPlayer(Long gameId, Long userId) {
        Game game = getGameById(gameId);

        Player player = game.getPlayers()
                .stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player " + userId + " not found in game " + gameId));

        player.addCoin();
        return player;
    }

    public Player buySongCard(Long gameId, Long userId) {
        Game game = getGameById(gameId);

        Player player = game.getPlayers()
                .stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player " + userId + " not found in game " + gameId));

        player.buySongCard();

        return player;
    }

    public Game createGame(Long lobbyId){
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Game game = new Game();
        game.setGameId(lobbyId);
        List<Player> players = game.createPlayers(lobby.getMembers(),apiHandler);
        game.createTurnOrder(players);
        game.setGameName(lobby.getLobbyName());
        game.setTurnCount(0);
        for (Player player : players){
            if (lobby.getHost().getId() == player.getUserId()){
                game.setHost(player);
            }
        }
        game = startNewRound(game);
        game = gameStorage.addGame(game);

        return game;
    }

    // TODO: depending on how we use it, we need to refactor this to get only the gameId and not the game? or to return the round instead of the whole game?
    public Game startNewRound(Game game) {
        SongCard newSongCard = apiHandler.getNewSongCard();
        game.startNewRound(newSongCard);
        return game;
    }

    public boolean checkGuess(Game game, Guess guess, Long userId) {
        boolean correct = guess.checkGuess(game);
        if (correct) {
            addCoinToPlayer(game.getGameId(),userId);
        }
        return correct;
    }
}
