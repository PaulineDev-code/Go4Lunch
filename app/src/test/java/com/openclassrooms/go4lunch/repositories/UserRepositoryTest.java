package com.openclassrooms.go4lunch.repositories;


import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.openclassrooms.go4lunch.helpers.UserHelper;
import com.openclassrooms.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserHelper userHelper;


    private UserRepository userRepository;

    @Before
    public void setUp() {


        given(userHelper.getUserData()).willReturn(mockedUserTask);
        given(mockedUserTask.isSuccessful()).willReturn(true);
        given(mockedUserTask.getResult()).willReturn(mockedUserTaskResult);
        given(mockedUserTask.getResult().exists()).willReturn(true);

        userRepository = new UserRepository(userHelper);
    }

    @Test
    public void nominal_case() throws InterruptedException {

        MutableLiveData<Boolean> resultUser = userRepository.getUser();

        verify(mockedUserTask).addOnCompleteListener(listenerUserArgumentCaptor.capture());

        listenerUserArgumentCaptor.getValue().onComplete(mockedUserTask);

        LiveDataTestUtils.observeForTesting(resultUser, liveData ->
                    assertEquals(true,  LiveDataTestUtils.getOrAwaitValue(resultUser)));

    }

    @Captor
    private ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> listenerUserArgumentCaptor;
    @Mock
    private Task<DocumentSnapshot> mockedUserTask;
    @Mock
    private DocumentSnapshot mockedUserTaskResult;


}
