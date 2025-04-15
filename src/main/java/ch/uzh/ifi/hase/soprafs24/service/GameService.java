package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.SongCard;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.storage.GameStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameService {

    // TODO: at the moment we log nothing here - remove logger or actually log activities?
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameStorage gameStorage;
    // TODO: we also do not need the userRepository, remove if we wont need in future
    private final UserRepository userRepository;
    private final APIHandler apiHandler;

    @Autowired
    public GameService(GameStorage gameStorage, UserRepository userRepository, APIHandler apiHandler) {
        this.gameStorage = gameStorage;
        this.userRepository = userRepository;
        this.apiHandler = apiHandler;
    }

    public Game getGameById(Long gameId){
        return gameStorage.getGameById(gameId);
    }

    public SongCard getSongCard(Long gameId) {
        Game game = getGameById(gameId);
        return game.getCurrentRound().getSongCard(); //get SongCard from currentRound
    }

    // TODO: depending on how we use it, we need to refactor this to get only the gameId and not the game? or to return the round instead of the whole game?
    public Game startNewRound(Game game) {
        SongCard newSongCard = apiHandler.getNewSongCard();
        game.startNewRound(newSongCard);
        return game;
    }

}
