package com.chatbot.application.views.chatbot;

import com.chatbot.application.views.MainLayout;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@PageTitle("Chatbot")
@Route(value = "", layout = MainLayout.class)
public class ChatbotView extends VerticalLayout {

    private static String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public ChatbotView() {
        setSpacing(false);

        TextField inputTextField = new TextField("Ask a question");
        inputTextField.setWidth("100%");

        TextArea outputTextArea = new TextArea("Chatbot response");
        outputTextArea.setWidth("100%");
        outputTextArea.setHeight("300px");
        outputTextArea.setReadOnly(true);

        Button submitButton = new Button("Submit", event -> {
            String inputText = inputTextField.getValue();
            String outputText = getChatbotResponse(inputText);
            outputTextArea.setValue(outputText);
        });

        add(new H2("Chatbot"), inputTextField, submitButton, outputTextArea);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private String getChatbotResponse(String inputText) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = String.format(
                "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}", inputText);
        RequestBody body = RequestBody.create(requestBody, mediaType);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            return parseChatbotResponse(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String parseChatbotResponse(String responseBody) {
        try {
            // Parse the JSON response and extract the chatbot's response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode choicesNode = rootNode.get("choices");
            if (choicesNode == null || choicesNode.size() == 0) {
                Dialog dialog = new Dialog();
                dialog.setCloseOnEsc(false);
                dialog.setCloseOnOutsideClick(false);

                TextField apiKeyField = new TextField("Enter a new API key");
                apiKeyField.setWidth("100%");

                Anchor apiKeyLink = new Anchor("https://platform.openai.com/account/api-keys", "Find your API key");
                apiKeyLink.setTarget("_blank");

                Button saveButton = new Button("Save", event -> {
                    API_KEY = apiKeyField.getValue();
                    dialog.close();
                });

                dialog.add(apiKeyField, apiKeyLink, saveButton);
                dialog.open();
                return "Error: No chatbot response found";
            }
            JsonNode messageNode = choicesNode.get(0).get("message");
            JsonNode contentNode = messageNode.get("content");
            return contentNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}