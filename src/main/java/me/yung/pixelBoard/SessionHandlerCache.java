package me.yung.pixelBoard;

import org.eclipse.jetty.websocket.common.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SessionHandlerCache {

    private static Map<String, SessionHandler> cache = new ConcurrentHashMap<>();

    static SessionHandlerCache getInstance() {
        return inst.ance;
    }

    void invalid(WebSocketSession session) {
        cache.remove(resolve(session).getSid());
    }

    SessionHandler resolve(WebSocketSession session) {
        SessionHandler sessionHandler = SessionHandler.wrap(session);
        if (cache.containsKey(sessionHandler.getSid())) {
            sessionHandler.setSession(session);
            return cache.get(sessionHandler.getSid());
        }
        cache.put(sessionHandler.getSid(), sessionHandler);
        return sessionHandler;
    }

    private static class inst {
        private static final SessionHandlerCache ance = new SessionHandlerCache();
    }
}
