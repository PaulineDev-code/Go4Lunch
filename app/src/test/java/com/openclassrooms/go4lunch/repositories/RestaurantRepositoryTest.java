package com.openclassrooms.go4lunch.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.models.maprestaurants.PlacesPOJO;
import com.openclassrooms.go4lunch.models.maprestaurants.Result;
import com.openclassrooms.go4lunch.service.ApiInterface;
import com.openclassrooms.go4lunch.service.RetrofitService;
import com.openclassrooms.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantRepositoryTest {

    private static final String TYPE_KEYWORD = "restaurant";
    private final String location = "48.844042,2.219498";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ApiInterface apiInterface;

    @InjectMocks
    private RestaurantRepository restaurantRepository;

    @Before
    public void setUp() {
        given(apiInterface.getNearbyPlaces(location, TYPE_KEYWORD, BuildConfig.apiKey))
                .willReturn(mockedCall);
        given(mockedResponse.body()).willReturn(mockedRestaurantResponse);
        given(mockedRestaurantResponse.getResults()).willReturn(mockedRestaurantResult);
    }

    @Test
    public void nominal_case() {

        // Ensure the apiInterface called by the repository will be our mocked interface
        try(MockedStatic<RetrofitService> retrofit = mockStatic(RetrofitService.class)) {
            retrofit.when(RetrofitService :: getInterface).thenReturn(apiInterface);
            // Given
            // Let's call the repository method
            MutableLiveData<List<Result>> result = restaurantRepository.getNearbyRestaurants(location);

            // Capture the callback waiting for data
            verify(apiInterface.getNearbyPlaces(location, TYPE_KEYWORD, BuildConfig.apiKey))
                    .enqueue(callbackArgumentCaptor.capture());

            // When
            // Trigger the response ourselves
            callbackArgumentCaptor.getValue().onResponse(mockedCall, mockedResponse);

            // Then
            // Assert the result is posted to the LiveData
            LiveDataTestUtils.observeForTesting(result, liveData ->
                    assertEquals(mockedRestaurantResult, liveData.getValue()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void onFailure_case() {
        // Ensure the apiInterface called by the repository will be our mocked interface
        try(MockedStatic<RetrofitService> retrofit = mockStatic(RetrofitService.class);
            MockedStatic<Log> log = mockStatic(Log.class)) {
            retrofit.when(RetrofitService :: getInterface).thenReturn(apiInterface);
            // Manage log in the onFailure
            log.when(() -> Log.e(anyString(), anyString(), any())).thenReturn(0);
            // Put the enqueued callback on failure
            lenient().when(mockedCall.isCanceled()).thenReturn(true);

            // Given
            // Let's call the repository method
            MutableLiveData<List<Result>> result = restaurantRepository.getNearbyRestaurants(location);

            // Capture the callback waiting for data
            verify(apiInterface.getNearbyPlaces(location, TYPE_KEYWORD, BuildConfig.apiKey))
                    .enqueue(callbackArgumentCaptor.capture());

            // When
            // Trigger the response ourselves
            callbackArgumentCaptor.getValue().onFailure(mockedCall, mock(Throwable.class));

            // Then
            // Assert the result is posted to the LiveData
            LiveDataTestUtils.observeForTesting(result, liveData -> assertEquals(new ArrayList<>(), liveData.getValue()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // region IN
    @Captor
    private ArgumentCaptor<Callback<PlacesPOJO>> callbackArgumentCaptor;
    @Mock
    private Call<PlacesPOJO> mockedCall;

    // region OUT
    @Mock
    private Response<PlacesPOJO> mockedResponse;
    @Mock
    private PlacesPOJO mockedRestaurantResponse;
    @Mock
    private List<Result> mockedRestaurantResult;
}
