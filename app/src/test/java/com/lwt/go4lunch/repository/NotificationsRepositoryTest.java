package com.lwt.go4lunch.repository;

import static android.content.Context.MODE_PRIVATE;
import static com.lwt.go4lunch.repository.NotificationsRepository.REMINDER_REQUEST;
import static com.lwt.go4lunch.repository.NotificationsRepository.SHARED_PREFS;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.lwt.go4lunch.LiveDataTestUtil;
import com.lwt.go4lunch.pojo.FavoriteRestaurant;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NotificationsRepositoryTest {
    @Rule // -> allows liveData to work on different thread besides main, must be public!
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    Context context;

    @Mock
    SharedPreferences sharedPreferences;

    NotificationsRepository repository;

    @Before
    public void setUp() throws Exception {
        when(context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)).thenReturn(sharedPreferences);
        repository = new NotificationsRepository(context);
    }

    @Test
    public void isNotificationEnabledLiveData() throws InterruptedException {
        when(sharedPreferences.getBoolean(REMINDER_REQUEST, false)).thenReturn(true);
        LiveData<Boolean> notificationLivedata = repository.isNotificationEnabledLiveData();
        Boolean notification = LiveDataTestUtil.getOrAwaitValue(notificationLivedata);
        assertEquals(true, notification);

        when(sharedPreferences.getBoolean(REMINDER_REQUEST, false)).thenReturn(false);
        LiveData<Boolean> notificationLivedata1 = repository.isNotificationEnabledLiveData();
        Boolean notification1 = LiveDataTestUtil.getOrAwaitValue(notificationLivedata1);
        assertEquals(false, notification1);
    }
}