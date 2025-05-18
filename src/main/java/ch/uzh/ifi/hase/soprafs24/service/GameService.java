package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.storage.GameStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GameService {

    private final GameStorage gameStorage;
    private final UserRepository userRepository;
    private final APIHandler apiHandler;
    private final LobbyService lobbyService;
    private final UserService userService;


    @Autowired
    public GameService(GameStorage gameStorage, UserRepository userRepository, LobbyService lobbyService, APIHandler apiHandler, UserService userService) {
        this.gameStorage = gameStorage;
        this.userRepository = userRepository;
        this.apiHandler = apiHandler;
        this.lobbyService = lobbyService;
        this.userService = userService;
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

    public void leaveOrDeleteGame(Long gameId, Long userId) {
        Game game = getGameById(gameId);
        User user = userService.getUserById(userId);
        if (user.getId().equals(game.getHost().getUserId())) {

            for (Player player : game.getPlayers()) {
                User playerUser = userService.getUserById(player.getUserId());
                playerUser.setStatus(UserStatus.ONLINE);
                userRepository.save(playerUser);
            }
            //delete Game
            gameStorage.deleteGame(gameId);
        }
        else {
            game.leaveGame(user);
            lobbyService.leaveOrDeleteLobby(gameId, userId);

            //if the leaving player was the active player -> new round with next player as active player
            if (game.getCurrentRound().getActivePlayer().getUserId().equals(userId)) {
                startNewRound(game);
            }

            user.setStatus(UserStatus.ONLINE);
            user.setLobbyId(null);
            userRepository.save(user);

        }
        userRepository.flush();
    }

    public Game updateActivePlayerPlacement(Game game, Integer placement) {
        game.getCurrentRound().setActivePlayerPlacement(placement);
        return game;
    }

    public Game updateChallengerPlacement(Game game, Integer placement) {
        game.getCurrentRound().setChallengerPlacement(placement);
        return game;
    }

    public boolean declinesChallenge(Long gameId, Long userId) {
        Game game = getGameById(gameId);
        Round round = game.getCurrentRound();
        round.userDeclinesChallenge(userId);
        Set<Long> declinedChallenge = round.getDeclinedChallenge();

        List<Long> nonActivePlayerIds = game.getPlayers().stream()
                .map(Player::getUserId)
                .filter(id -> !id.equals(round.getActivePlayer().getUserId()))
                .toList();

        //if all players declined => return true, else false:
        return declinedChallenge.containsAll(nonActivePlayerIds);
    }
}
