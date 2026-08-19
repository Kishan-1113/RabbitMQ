package com.example.rabbit_config.Service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final ChatModel chatModel;

    public GeminiService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String getGeminiResponse(String prompt) {
        ChatResponse response = chatModel.call(
                new Prompt(
                        prompt,
                        GoogleGenAiChatOptions.builder()
                                .temperature(0.4)
                                .build()));

        return response.getResult().getOutput().getText();
    }

    // public String analyzeImage(MultipartFile file, String prompt) {
    // try {
    // // Convert image to Resource
    // ByteArrayResource resource = new ByteArrayResource(file.getBytes());

    // // 1. Create the Media object (ensure correct Spring AI import)
    // Media media = new
    // Media(org.springframework.util.MimeTypeUtils.parseMimeType(file.getContentType()),
    // resource);

    // // 2. Use the BUILDER instead of the constructor
    // UserMessage message = UserMessage.builder()
    // .text(prompt)
    // .media(media)
    // .build();

    // // 3. Call the model
    // ChatResponse response = chatModel.call(
    // new Prompt(
    // message,
    // GoogleGenAiChatOptions.builder()
    // .temperature(0.4)
    // .build()));

    // return response.getResult().getOutput().getText();

    // } catch (Exception e) {
    // e.printStackTrace();
    // return "Error processing image: " + e.getMessage();
    // }
    // }
}