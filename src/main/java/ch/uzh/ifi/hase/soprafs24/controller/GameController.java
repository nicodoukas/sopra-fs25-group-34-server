package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SongCardGetDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerGetDTO;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    private final GameService gameService;

    private final UserService userService;

    GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable Long gameId) {

        Game game=gameService.getGameById(gameId);
        return DTOMapper.INSTANCE.convertEntitytoGameGetDTO(game);
    }

    @GetMapping("/game/{gameId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerGetDTO getPlayerInGame(@PathVariable Long gameId, @PathVariable Long userId) {
        Player player = gameService.getPlayerInGame(gameId, userId);
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player);
    }

    @GetMapping("games/{gameId}/song")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SongCardGetDTO getSongCard(@PathVariable Long gameId) {
        SongCard songCard = gameService.getSongCard(gameId);
        return DTOMapper.INSTANCE.convertEntityToSongCardGetDTO(songCard);
    }

}
