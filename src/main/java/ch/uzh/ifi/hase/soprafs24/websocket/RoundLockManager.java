package ch.uzh.ifi.hase.soprafs24.websocket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;


@Component
public class RoundLockManager {
    private final Set<Long> inProgressGames = ConcurrentHashMap.newKeySet();

    public boolean tryLock(Long gameId) {
        return inProgressGames.add(gameId); // returns false if already present
    }

    public void unlock(Long gameId) {
        inProgressGames.remove(gameId);
    }
}

