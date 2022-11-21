package com.openclassrooms.go4lunch.utils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtils {

    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        // Don't wait indefinitely if the LiveData is not set.
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }

    public static <T> void observeForTesting(LiveData<T> liveData, OnObservedListener<T> onObservedListener) throws InterruptedException {
        boolean[] called = {false};

        liveData.observeForever(value -> called[0] = true);

        onObservedListener.onObserved(liveData);

        if (!called[0]) {
            fail("LiveData didn't emit any value");
        }
    }

    public interface OnObservedListener<T> {
        void onObserved(LiveData<T> liveData) throws InterruptedException;
    }
}
