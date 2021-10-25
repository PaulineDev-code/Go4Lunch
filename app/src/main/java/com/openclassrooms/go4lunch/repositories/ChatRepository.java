package com.openclassrooms.go4lunch.repositories;


import com.google.firebase.firestore.Query;
import com.openclassrooms.go4lunch.helpers.ChatHelper;



public class ChatRepository {

    private final ChatHelper chatHelper = ChatHelper.getInstance();
    private static ChatRepository CHAT_REPOSITORY;

    //Instance of Repository
    public static ChatRepository getInstance() {
        if (CHAT_REPOSITORY == null) {
            CHAT_REPOSITORY = new ChatRepository();
        }
        return CHAT_REPOSITORY;
    }

    public Query getAllMessageForChat(){
        return chatHelper.getAllMessageForChat();
    }

    public void createMessageForChat(String message){
        chatHelper.createMessageForChat(message);
    }
}
