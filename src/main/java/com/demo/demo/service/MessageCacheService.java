package com.demo.demo.service;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.demo.entity.Chat.ChatMessage;

@Service
public class MessageCacheService {
    // Them so luong tin nhan toi da trong cache
    private static final int MAX_SIZE = 30;
    private final Deque<ChatMessage> cache = new LinkedList<>();

    public synchronized void add(ChatMessage msg) {
        cache.addLast(msg);
        if (cache.size() > MAX_SIZE) {
            cache.removeFirst();
        }
    }

    public synchronized List<ChatMessage> getHistory() {
        return new ArrayList<>(cache);
    }
    
    public synchronized void clear() {
        cache.clear();
    }
}
