package com.demo.demo.api;

import com.demo.demo.entity.Chat.ChatMessage;
import com.demo.demo.entity.Chat.ChatMessage.MessageType;
import com.demo.demo.service.MessageCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@Controller
public class ChatController {

        @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageCacheService cacheService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage incoming, Principal principal) {
        // Use sender from incoming message (from frontend) instead of Principal
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(MessageType.CHAT);
        chatMessage.setSender(incoming.getSender() != null ? incoming.getSender() : "Anonymous");
        chatMessage.setContent(incoming.getContent());
        chatMessage.setTimestamp(Instant.now().toString());
        chatMessage.setAvatarUrl(incoming.getAvatarUrl()); // Add avatarUrl from frontend

        System.out.println("Broadcasting message: " + chatMessage.getContent() + " from " + chatMessage.getSender());
        
        // Lưu vào cache và broadcast
        cacheService.add(chatMessage);
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }

     @MessageMapping("/chat.addUser")
    @SendToUser("/queue/history")
    public List<ChatMessage> addUser(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        String username = principal != null ? principal.getName() : "Anonymous";
        
        System.out.println("User joining chat: " + username);
        
        // Lưu username vào session để listener gửi LEAVE sau này
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("username", username);
        }

        // Không broadcast JOIN để tránh spam tin nhắn
        // ChatMessage joinMsg = new ChatMessage();
        // joinMsg.setType(MessageType.JOIN);
        // joinMsg.setSender(username);
        // joinMsg.setTimestamp(Instant.now().toString());
        // messagingTemplate.convertAndSend("/topic/public", joinMsg);

        // Trả về lịch sử (30 tin gần nhất) cho chính user này
        List<ChatMessage> history = cacheService.getHistory();
        System.out.println("Sending chat history: " + history.size() + " messages");
        return history;
    }

}