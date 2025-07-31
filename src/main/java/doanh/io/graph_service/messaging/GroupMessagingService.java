package doanh.io.graph_service.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class GroupMessagingService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendCreateChatRoom(String userId, Object payload) {
        messagingTemplate.convertAndSend("/topic/create-chat-room/" + userId, payload);
    }

    public void sendNotify(String userId, Object payload) {
        messagingTemplate.convertAndSend("/topic/notify/" + userId, payload);
    }
}
