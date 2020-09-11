package ru.kpfu.itis.group_907.gadelshin;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Client client = new Client();
        Map<String, String> mapHeader = new HashMap<>();
        Map<String, String> mapParams = new HashMap<>();

        mapHeader.put("nameKey", "value");
        mapParams.put("once", "oncebutbody");
        mapParams.put("twice", "twicebutbody");
        String urlGet = "https://postman-echo.com/get";
        String urlPost = "https://postman-echo.com/post";

        System.out.println(client.get(urlGet, mapHeader, mapParams));
        System.out.println(client.post(urlPost, mapHeader, mapParams));
    }
}
