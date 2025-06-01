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


    /**
     * Retrieves the response from the Ollama API.
     *
     * @return the response as a String
     */
    public String getResponse() {
        return response;
    }

    /**
     * Parses a JSON string into an instance of {@code OllamaResponse}.
     *
     * @param body the JSON string representing the response
     * @return an {@code OllamaResponse} object corresponding to the parsed JSON input
     */
    public static OllamaResponse fromJson(String body) {
        //for documentation of how to decode JSON response https://github.com/ollama/ollama/blob/main/docs/api.md
        Gson gson = new Gson();
        return gson.fromJson(body, OllamaResponse.class);
    }
}