package com.openclassrooms.go4lunch.helpers;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.openclassrooms.go4lunch.models.Message;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.repositories.UserRepository;

public class ChatHelper {

    private static final String MESSAGE_COLLECTION = "messages";
    private static ChatHelper CHAT_HELPER;
    private UserRepository userRepository;

    private ChatHelper() {this.userRepository = UserRepository.getInstance();}


    //Instance for Repository
    public static ChatHelper getInstance() {
        if (CHAT_HELPER == null) {
            CHAT_HELPER = new ChatHelper();
        }
        return CHAT_HELPER;
    }

    public CollectionReference getMessageCollection(){
        return FirebaseFirestore.getInstance().collection(MESSAGE_COLLECTION);
    }

    //Get all last messages to display
    public Query getAllMessageForChat(){
        return this.getMessageCollection()
                .orderBy("dateCreated")
                .limit(50);
    }

    //Create a new message
    public void createMessageForChat(String textMessage){

        userRepository.getUserData().addOnSuccessListener(user -> {
            // Create the Message object
            String username = user.getName();
            Message message = new Message(textMessage, user);

            // Store Message to Firestore
            ChatHelper.this.getMessageCollection().add(message);
        });
    }
}
