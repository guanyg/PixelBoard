package me.yung.pixelBoard;

import java.util.UUID;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        webSocket("/ws", PixelBoardWs.class);
        staticFileLocation("static");
        exception(Exception.class, (exception, request, response) -> exception.printStackTrace());
        get("/bind-session", (request, response) -> UUID.randomUUID().toString());
    }
}
