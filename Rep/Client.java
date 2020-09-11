package ru.kpfu.itis.group_907.gadelshin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Client implements  HTTPClient{

    public String get(String url, Map<String, String> headers, Map<String, String> params) {
        try {
            URL getURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) getURL.openConnection();

            connection.setRequestMethod("GET");

            for (String key : headers.keySet()) {
                connection.addRequestProperty(key, headers.get(key));
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();

        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String post(String url, Map<String, String> headers, Map<String, String> params) {
        try {
            StringBuilder param = new StringBuilder();

            for (String s : params.keySet()) {
                param.append(s).append("=").append(params.get(s)).append("&");
            }

            param.delete(param.length() - 1, param.length());

            byte[] post = param.toString().getBytes(StandardCharsets.UTF_8);
            URL urlPOST = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlPOST.openConnection();

            for (String key : headers.keySet()) {
                connection.addRequestProperty(key, headers.get(key));
            }

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(post);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();

        } catch (ProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
