package WebSocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/websocket")
public class DataWebSocket {

    private static final Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Можно обработать входящие сообщения
    }
    // При ошибке в соединении
    @OnError
    public void onError(Session session, Throwable throwable) {
        clients.remove(session);
    }

    public static void broadcast(String message) {
        synchronized (clients) {
            for (Session session : clients) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
