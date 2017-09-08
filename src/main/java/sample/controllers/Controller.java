package sample.controllers;

import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import sample.DialogUtils;
import sample.models.ChatSocket;
import sample.models.IMessageObserver;
import sample.models.MessageFactory;


import java.lang.reflect.Type;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, IMessageObserver {

    private ChatSocket socket;

    @FXML
    TextArea textArea1;

    @FXML
    Button sendButton1;

    @FXML
    TextArea textMessage1;

    @FXML
    TextArea textAreaUserLogged;



    public Controller() {
        socket = ChatSocket.getSocket();
    }

    public void initialize(URL location, ResourceBundle resources) {
        socket.connect();
        sendNickPacket(DialogUtils.createNickDIalog(null));
        textArea1.requestFocus();
        textMessage1.setWrapText(true);
        textAreaUserLogged.setWrapText(true);
        socket.setObserver(this);

        sendButton1.setOnMouseClicked(s -> {
            sendMessagePacket(textMessage1.getText());
            textMessage1.clear();
        });
    }


    @Override
    public void handleMessager(String s) {
        Type token = new TypeToken<MessageFactory>() {
        }.getType();
        MessageFactory factory = MessageFactory.GSON.fromJson(s, token);

        switch (factory.getMessageType()) {
            case SEND_MESSAGE: {
                textArea1.appendText(("\n" + factory.getMessage()));
                break;
            }
            case NICK_NOT_FREE:{
                //todo zmiana
                Platform.runLater(()-> sendNickPacket(DialogUtils.createNickDIalog(factory.getMessage())));
                break;

            }
            case USER_JOIN: {
                //ten case sie przyda gdy bedizemy miec liste obok jako widok
                textArea1.appendText("~~>" + factory.getMessage() + "<~~"+"\n");
                break;
            }
            case USER_LEFT: {
                //ten case sie przyda gdy bedizemy miec liste obok jako widok
                textArea1.appendText("<~~" + factory.getMessage() + "~~>"+"\n");
                break;
            }

        }

    }

    private void sendNickPacket(String nick){
        MessageFactory factory = new MessageFactory();
        factory.setMessageType(MessageFactory.MessageType.SET_NICK);
        factory.setMessage(nick);

        sendMessage(factory);
    }
    private void sendMessagePacket(String message) {
        MessageFactory factory = new MessageFactory();
        factory.setMessageType(MessageFactory.MessageType.SEND_MESSAGE);
        factory.setMessage(message);
        sendMessage(factory);

    }

    private void sendMessage(MessageFactory factory) {
        socket.sendMessage(MessageFactory.GSON.toJson(factory));

    }

}