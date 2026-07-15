package com.example.demo.servlet;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class GameSessionStore {
    static class Entry {
        String token;
        String phone;
        long seed;
        Instant expiresAt;
    }

    private static final Map<String, Entry> TOKENS = new ConcurrentHashMap<>();

    static Entry create(String phone, long seed, long ttlSeconds) {
        Entry e = new Entry();
        e.token = UUID.randomUUID().toString().replace("-", "");
        e.phone = phone;
        e.seed = seed;
        e.expiresAt = Instant.now().plusSeconds(ttlSeconds);
        TOKENS.put(e.token, e);
        return e;
    }

    static Entry get(String token) {
        if (token == null) return null;
        Entry e = TOKENS.get(token);
        if (e == null) return null;
        if (Instant.now().isAfter(e.expiresAt)) {
            TOKENS.remove(token);
            return null;
        }
        return e;
    }

    static void remove(String token) {
        if (token != null) TOKENS.remove(token);
    }
}