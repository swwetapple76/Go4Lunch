package com.lwt.go4lunch.ui.chat;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lwt.go4lunch.LiveDataTestUtil;
import com.lwt.go4lunch.model.ChatMessageModel;
import com.lwt.go4lunch.model.UserModel;
import com.lwt.go4lunch.repository.ChatMessageRepository;
import com.lwt.go4lunch.usecase.AddChatMessageToFirestoreUseCase;
import com.lwt.go4lunch.usecase.GetCurrentUserIdUseCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ChatViewModelTest {

    @Rule // -> allows liveData to work on different thread besides main, must be public!
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    ChatMessageRepository chatMessageRepository;
    @Mock
    GetCurrentUserIdUseCase getCurrentUserIdUseCase;
    @Mock
    AddChatMessageToFirestoreUseCase addChatMessageToFirestoreUseCase;

    ChatViewModel chatViewModel;

    @Before
    public void setUp() throws Exception {
        chatViewModel = new ChatViewModel(chatMessageRepository, getCurrentUserIdUseCase, addChatMessageToFirestoreUseCase);
    }

    @Test
    public void getChatMessages() throws InterruptedException {
        String currentUserId = "currentUserId";

        List<ChatMessageModel> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessageModel("message1", "workmateId", "date1", 0L));
        chatMessages.add(new ChatMessageModel("message2", "workmateId", "date2", 0L));
        LiveData<List<ChatMessageModel>> chatMessagesLivedata = new MutableLiveData<>(chatMessages);
        when(chatMessageRepository.getChatMessages("workmateId")).thenReturn(chatMessagesLivedata);

        when(getCurrentUserIdUseCase.invoke()).thenReturn(currentUserId);

        chatViewModel.init("workmateId");

        LiveData<List<ChatViewState>> chatsLiveData = chatViewModel.getChatMessages();
        List<ChatViewState> chats = LiveDataTestUtil.getOrAwaitValue(chatsLiveData);

        assertEquals(2, chats.size());
    }
}