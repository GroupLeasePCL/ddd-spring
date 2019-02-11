package com.example.lunchtimeboot.infrastructure.client.restaurant.http;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockserver.client.MockServerClient;

import java.util.UUID;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockServerFixture {

    public void mockServerGetRestaurant(String host, int port) throws JSONException {
        // first user
        UUID restaurantId = UUID.fromString("99999999-9999-9999-9999-000000000000");
        JSONObject locationJson = new JSONObject()
                .put("latitude", 100.000001)
                .put("longitude", 100.000001);
        JSONObject responseBody = new JSONObject()
                .put("id", restaurantId.toString())
                .put("name", "Bon Mache")
                .put("location", locationJson);
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/restaurant/api/restaurants/" + restaurantId.toString())
                )
                .respond(
                        response()
                                .withBody(responseBody.toString())
                );
    }
}
