package me.yung.pixelBoard;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.common.WebSocketSession;

import java.io.IOException;
import java.util.List;

public class SessionHandler implements FrameListener {
    private String sid;
    private FrameWrapper frame;
    private WebSocketSession session;

    static SessionHandler wrap(WebSocketSession session) {
        SessionHandler ret = new SessionHandler();
        ret.session = session;
        ret.sid = extractSessionId(ret.session);
        return ret;
    }

    private static String extractSessionId(WebSocketSession session) {
        return session.getRequestURI().getQuery();
    }

    String getSid() {
        return sid;
    }

    FrameWrapper getFrame() {
        return frame;
    }

    synchronized void swapFrame(FrameWrapper frame) {
        if (this.frame != null) {
            this.frame.removeListener(this);
        }
        frame.addListener(this);
        this.frame = frame;
        try {
            this.session.getRemote().sendString("FLUSH#" + new Gson().toJson(frame.getFrame()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFrameUpdate(List<FrameIncrement> ev) {
        for (FrameIncrement inc :
                ev) {
            try {
                session.getRemote().sendString(inc.buildMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SessionHandler) {
            SessionHandler sessionHandler = (SessionHandler) obj;
            return sessionHandler.sid.equals(this.sid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.sid.hashCode();
    }

    void setSession(WebSocketSession session) {
        this.session = session;
    }
}
