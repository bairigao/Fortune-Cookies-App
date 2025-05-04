package com.example.fortune_cookies_app.model;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * {@code OllamaClient} simple HTTP client for interacting with an
 * Ollama AI model. It sends user prompts to the Ollama /api/generate endpoint
 * and extracts the AI-generated response from the JSON payload.
 * Usage example:
 * <pre>
 * String reply = OllamaClient.ask("Hello, Ollama!");
 * System.out.println(reply);
 * </pre>
 */
public class OllamaClient {
    /**
     * Sends a prompt to the Ollama server and returns the AI-generated content.
     *
     * @param prompt the user-provided prompt to send to the AI model
     * @return the textual response extracted from the Ollama JSON reply
     * @throws Exception if an I/O error occurs or the HTTP request fails
     */
    public static String ask(String prompt) throws Exception {
        String json = """
        {
          "model": "gemma3:4b",
          "prompt": "%s",
          "stream": false
        }
        """.formatted(prompt.replace("\"", "\\\""));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:11434/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return extractResponse(response.body());
    }

    /**
     * Parses the raw JSON string returned by the Ollama server and extracts
     * the value of the "response" field, along with handling simple markdown complications.
     *
     * @param json the raw JSON response from the Ollama server
     * @return the unescaped textual content of the "response" field,
     *         or a fallback message if the field is not found
     */
    private static String extractResponse(String json) {
        int start = json.indexOf("\"response\":\"");
        if (start == -1) return "No response field found.";

        start += "\"response\":\"".length();
        StringBuilder result = new StringBuilder();
        boolean escape = false;

        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escape) {
                switch (c) {
                    case 'n' -> result.append('\n');
                    case 't' -> result.append('\t');
                    case 'r' -> result.append('\r');
                    case 'b' -> result.append('\b');
                    case 'f' -> result.append('\f');
                    case '\\' -> result.append('\\');
                    case '"' -> result.append('\"');
                    case 'u' -> {
                        if (i + 4 < json.length()) {
                            String hex = json.substring(i + 1, i + 5);
                            try {
                                result.append((char) Integer.parseInt(hex, 16));
                                i += 4;
                            } catch (NumberFormatException ignored) {
                                result.append("\\u").append(hex);
                            }
                        }
                    }
                    default -> result.append(c);
                }
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == '"') {
                break;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

}
