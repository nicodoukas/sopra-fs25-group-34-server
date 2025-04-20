package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SongCardGetDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SongCardInsertDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;

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
    public void insertSongCardIntoTimeline(@PathVariable Long gameId,
                                           @PathVariable Long userId,
                                           @RequestBody SongCardInsertDTO insertDTO) {
        gameService.insertSongCardIntoTimeline(gameId, userId, insertDTO.getSongCard(), insertDTO.getPosition());
    }



    @MessageMapping("/createGame")
    public GameGetDTO createGame(Long lobbyId){
        Game game = gameService.createGame(lobbyId);
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntitytoGameGetDTO(game);
        webSocketMessenger.sendMessage("/games/"+ lobbyId, "game-created", gameGetDTO);
        return gameGetDTO;
    }

}
