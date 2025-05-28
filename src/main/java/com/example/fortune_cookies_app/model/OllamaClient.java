package com.example.fortune_cookies_app.model;


/**
 * Handles AI interaction with the Ollama HTTP server.
 * Supports both synchronous and asynchronous prompts.
 */
public class OllamaClient {
    private static final String API_URL = "http://localhost:11434/api/generate";
    private static final String DEFAULT_MODEL = new OllamaResponseFetcher("http://localhost:11434/api/generate").getDefaultInstalledModel();
    private static final OllamaResponseFetcher fetcher = new OllamaResponseFetcher(API_URL);

    /**
     * Sends a synchronous prompt to the Ollama server.
     *
     * @param prompt the text to send
     * @return the AI-generated response or an error message
     */
    public static String ask(String prompt) {
        OllamaResponse response = fetcher.fetchOllamaResponse(DEFAULT_MODEL, prompt);
        return (response != null && response.getResponse() != null)
                ? response.getResponse()
                : "No response received.";
    }

    /**
     * Sends a prompt to the Ollama server asynchronously.
     *
     * @param prompt the text to send
     * @param listener the callback for handling the response
     */
    public static void askAsync(String prompt, ResponseListener listener) {
        fetcher.fetchAsynchronousOllamaResponse(DEFAULT_MODEL, prompt, listener);
    }
}