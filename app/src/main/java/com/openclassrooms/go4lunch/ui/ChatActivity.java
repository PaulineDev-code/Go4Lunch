package com.openclassrooms.go4lunch.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.openclassrooms.go4lunch.databinding.ActivityChatBinding;
import com.openclassrooms.go4lunch.models.Message;
import com.openclassrooms.go4lunch.repositories.ChatRepository;
import com.openclassrooms.go4lunch.repositories.UserRepository;
import com.openclassrooms.go4lunch.ui.chat.ChatAdapter;

public class ChatActivity extends BaseActivity<ActivityChatBinding> implements ChatAdapter.Listener {

    private ChatAdapter chatAdapter;
    private String currentChatName;

/*    private static final String CHAT_NAME_ANDROID = "android";
    private static final String CHAT_NAME_BUG = "bug";
    private static final String CHAT_NAME_FIREBASE = "firebase";*/

    private UserRepository userRepository = UserRepository.getInstance();
    private ChatRepository chatRepository = ChatRepository.getInstance();

    @Override
    protected ActivityChatBinding getViewBinding() {
        return ActivityChatBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureRecyclerView();
        setupListeners();
    }

    private void setupListeners(){
        // Send button
        binding.sendButton.setOnClickListener(view -> { sendMessage(); });
    }

    private void sendMessage(){
        // Check if user can send a message (Text not null + user logged)
        boolean canSendMessage = !TextUtils.isEmpty(binding.chatEditText.getText()) && userRepository.isCurrentUserLogged();

        if (canSendMessage){
            // Create a new message for the chat
            chatRepository.createMessageForChat(binding.chatEditText.getText().toString());
            // Reset text field
            binding.chatEditText.setText("");
        }
    }

    // Configure RecyclerView
    private void configureRecyclerView(){
        //Track current chat name
        /*this.currentChatName = chatName;*/
        //Configure Adapter & RecyclerView
        this.chatAdapter = new ChatAdapter(
                generateOptionsForAdapter(chatRepository.getAllMessageForChat()),
                Glide.with(this), this);

        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });

        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRecyclerView.setAdapter(this.chatAdapter);
    }

    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
    }

    public void onDataChanged() {
        // Show TextView in case RecyclerView is empty
        binding.emptyRecyclerView.setVisibility(this.chatAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

}
