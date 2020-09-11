package ru.kpfu.itis.group_907.gadelshin;

import java.util.List;
import java.util.Map;

public interface HTTPClient {
    public String get(String url, Map<String, String> headers, Map<String, String> params);

    public String post(String url, Map<String, String> headers, Map<String, String> params);
}
