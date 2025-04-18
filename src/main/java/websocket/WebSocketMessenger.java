package ch.uzh.ifi.hase.soprafs24.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;


@Service
public class WebSocketMessenger {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketMessenger(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String destination, String event_type, Object data){
        Map<String, Object> message = new HashMap<>();
        message.put("event_type", event_type);
        message.put("data", data);
        messagingTemplate.convertAndSend(destination, message);
    }

}
