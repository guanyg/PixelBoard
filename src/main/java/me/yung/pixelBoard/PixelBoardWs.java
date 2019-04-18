package me.yung.pixelBoard;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.common.WebSocketSession;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class PixelBoardWs {
    private final static Map<String, FrameWrapper> frames = new ConcurrentHashMap<>();
    private static final int DEFAULT_WIDTH = 60;
    private static final int DEFAULT_HEIGHT = 30;
    private final SessionHandlerCache sessionHandlerCache = SessionHandlerCache.getInstance();

    public PixelBoardWs() {
        synchronized (frames) {
            if (!frames.containsKey("FRAME1")) {
                Frame initFrame = null;
                try (InputStreamReader streamReader = new InputStreamReader(PixelBoardWs.class.getResourceAsStream("/initFrame.json"))) {
                    initFrame = new Gson().fromJson(streamReader, Frame.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (initFrame == null) {
                    initFrame = new Frame(DEFAULT_HEIGHT, DEFAULT_WIDTH);
                }
                frames.put("FRAME1", new FrameWrapper(initFrame));
            }
        }
    }

    @OnWebSocketConnect
    public void connected(Session session) {
        sessionHandlerCache.resolve((WebSocketSession) session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        SessionHandler sessionHandler = sessionHandlerCache.resolve((WebSocketSession) session);

        if (message.length() < 3)
            return;

        switch (message.substring(0, 3)) {
            case "INC":
                if (sessionHandler.getFrame() != null) {
                    sessionHandler.getFrame().update(FrameIncrement.parse(message));
                }
                break;
            case "FRK":
                if (sessionHandler.getFrame() != null) {
                    FrameWrapper newFrame = sessionHandler.getFrame().fork();
                    frames.put(newFrame.getFrameId(), newFrame);
                    sessionHandler.swapFrame(newFrame);
                }
                break;
            case "CON":
                FrameWrapper frame = frames.get(message.split("#")[1]);
                if (frame != null) {
                    sessionHandler.swapFrame(frame);
                }
                break;
            case "NEW":
                FrameWrapper newFrame = new FrameWrapper(new Frame(DEFAULT_HEIGHT, DEFAULT_WIDTH));
                sessionHandler.swapFrame(newFrame);
            default:
        }
    }


    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessionHandlerCache.invalid((WebSocketSession) session);
    }
}
