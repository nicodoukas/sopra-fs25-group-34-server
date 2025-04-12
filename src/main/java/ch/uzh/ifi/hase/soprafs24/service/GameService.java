package ch.uzh.ifi.hase.soprafs24.service;

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

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameStorage gameStorage;
    private final UserRepository userRepository;

    @Autowired
    public GameService(GameStorage gameStorage, UserRepository userRepository) {
        this.gameStorage = gameStorage;
        this.userRepository = userRepository;
    }
}
