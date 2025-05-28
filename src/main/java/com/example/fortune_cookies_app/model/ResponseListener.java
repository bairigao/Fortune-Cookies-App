package com.example.fortune_cookies_app.model;


/**
 * Interface to define the response listener
 */
public interface ResponseListener {
    /**
     * Handles the response received from the Ollama API.
     *
     * @param response the response object containing information returned by the Ollama API
     */
    public void onResponseReceived(OllamaResponse response);
}
