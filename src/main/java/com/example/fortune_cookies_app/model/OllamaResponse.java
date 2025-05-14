package com.example.fortune_cookies_app.model;
import com.google.gson.Gson;

/**
 * Class to define the response from the Ollama API
 */
public class OllamaResponse {

    public String model;
    public String created_at;
    public String response;
    public String done;


    public String getResponse() {
        return response;
    }

    public static OllamaResponse fromJson(String body) {
        //for documentation of how to decode JSON response https://github.com/ollama/ollama/blob/main/docs/api.md
        Gson gson = new Gson();
        OllamaResponse response = gson.fromJson(body, OllamaResponse.class);
        return response;
    }
}