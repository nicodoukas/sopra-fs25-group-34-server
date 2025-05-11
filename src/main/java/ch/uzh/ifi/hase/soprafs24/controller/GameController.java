package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Guess;
import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

@RestController
public class GameController {

    private final GameService gameService;

    private final UserService userService;

    private final WebSocketMessenger webSocketMessenger;

    GameController(GameService gameService, UserService userService, WebSocketMessenger webSocketMessenger) {
        this.gameService = gameService;
        this.userService = userService;
        this.webSocketMessenger = webSocketMessenger;
    }

    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable Long gameId) {

        Game game=gameService.getGameById(gameId);
        return DTOMapper.INSTANCE.convertEntitytoGameGetDTO(game);
    }

    @GetMapping("/games/{gameId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerGetDTO getPlayerInGame(@PathVariable Long gameId, @PathVariable Long userId) {
        Player player = gameService.getPlayerInGame(gameId, userId);
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player);
    }

    @GetMapping("/games/{gameId}/song")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SongCardGetDTO getSongCard(@PathVariable Long gameId) {
        SongCard songCard = gameService.getSongCard(gameId);
        return DTOMapper.INSTANCE.convertEntityToSongCardGetDTO(songCard);
    }

    @PutMapping("/games/{gameId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerGetDTO updatePlayer(@PathVariable Long gameId,
                                     @PathVariable Long userId,
                                     @RequestBody PlayerPutDTO playerPutDTO) {
        Player updatedPlayer;
        // You call this function from the client either to add a coin, or to update the timeline
        if (playerPutDTO.getAddCoin()) {
            updatedPlayer = gameService.addCoinToPlayer(gameId, userId);
        } else {
            updatedPlayer = gameService.insertSongCardIntoTimeline(
                    gameId,
                    userId,
                    playerPutDTO.getSongCard(),
                    playerPutDTO.getPosition()
            );
        }
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(updatedPlayer);
    }

    @PutMapping("/games/{gameId}/buy")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerGetDTO buySongCard(@PathVariable Long gameId, @RequestBody String userId) {
        Long userIdLong = Long.valueOf(userId);
        Player updatedPlayer = gameService.buySongCard(gameId, userIdLong);
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(updatedPlayer);
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@RequestBody Long lobbyId) {
        Game game = gameService.createGame(lobbyId);
        return DTOMapper.INSTANCE.convertEntitytoGameGetDTO(game);
    }


    /*@MessageMapping("/createGame")
    public GameGetDTO createGame(Long lobbyId){
        Game game = gameService.createGame(lobbyId);
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntitytoGameGetDTO(game);
        webSocketMessenger.sendMessage("/games/"+ lobbyId, "game-created", gameGetDTO);
        return gameGetDTO;
    }*/

    @MessageMapping("/play")
    public void playSong(String gameId){
        webSocketMessenger.sendMessage("/games/"+gameId, "play-song", null);
    }

    @MessageMapping("/start")
    public void startNotification(String lobbyId){
        webSocketMessenger.sendMessage("/games/"+lobbyId, "start-game", null);
    }

    @PostMapping("/games/{gameId}/{userId}/guess")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean checkGuess(@PathVariable Long gameId, @PathVariable Long userId, @RequestBody GuessPostDTO guessPostDTO) {
       Game game = gameService.getGameById(gameId);
       Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);
       return gameService.checkGuess(game, guess, userId);
    }

    @DeleteMapping("/games/{gameId}/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable Long gameId, @PathVariable Long userId) {
        gameService.leaveOrDeleteGame(gameId, userId);
    }

    @MessageMapping("/startNewRound")
    public void startNewRound(String gameId){
        Game game = gameService.getGameById(Long.valueOf(gameId));
        gameService.startNewRound(game);
        webSocketMessenger.sendMessage("/games/"+gameId, "start-new-round", null);
    }

    @MessageMapping("/delete")
    public void deleteLobby(String lobbyId) {
        System.out.println("Backend: Deleting lobby with ID " + lobbyId);
        webSocketMessenger.sendMessage("/games/"+lobbyId, "delete-lobby", null);
    }
    @MessageMapping("/updatelobby")
    public void updateLobby(String lobbyId){
        webSocketMessenger.sendMessage("/games/"+lobbyId, "update-lobby", null);
    }
    @MessageMapping("/startchallenge")
    public void startChallenge(PlacementMessage placementMessage){
        Game game = gameService.getGameById(Long.valueOf(placementMessage.getGameId()));
        game = gameService.updateAcivePlayerPlacement(game, placementMessage.getPlacement());
        webSocketMessenger.sendMessage("/games/"+placementMessage.getGameId(), "start-challenge", game);
    }

    @MessageMapping("/deleteGame")
    public void deleteGame(String gameId) {
        webSocketMessenger.sendMessage("/games/" + gameId, "delete-game", null);
    }

    @MessageMapping("/updategame")
    public void updateGame(String gameId) {
        webSocketMessenger.sendMessage("/games/" + gameId, "update-game", null);
    }
    @MessageMapping("/challenge/accept")
    public void handleChallengeAccepted(@Payload Map<String, String> body){
        Long gameId = Long.parseLong(body.get("gameId"));
        Long userId = Long.parseLong(body.get("userId"));
        Game game = gameService.getGameById(gameId);
        Round round = game.getCurrentRound();
        Player challenger = gameService.getPlayerInGame(gameId, userId);

        if (round.getChallenger()==null){
            round.setChallenger(challenger);
            webSocketMessenger.sendMessage("/games/"+gameId, "challenge-accepted", game);
        }
        else{
            webSocketMessenger.sendMessage("/games/"+gameId, "challenge-denied", userId);
        }

    }

    @MessageMapping("/backToLobby")
    public void backToLobby(String gameId) {
        webSocketMessenger.sendMessage("/games/" + gameId, "back-to-lobby", null);
    }

}
