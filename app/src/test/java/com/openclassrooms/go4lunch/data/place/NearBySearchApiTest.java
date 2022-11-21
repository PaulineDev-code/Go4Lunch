package com.openclassrooms.go4lunch.data.place;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;

public class NearBySearchApiTest {

    /* This test class was made for trying the mocking web server feature it is not an actual test
    class
    The actual test class for this data is RestaurantRepositoryTest */


    public MockWebServer mockWebServer;

    @Before
    public void setUp() {
        mockWebServer = new MockWebServer();
        //Api to set
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
