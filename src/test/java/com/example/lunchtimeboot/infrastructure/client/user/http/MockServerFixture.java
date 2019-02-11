package com.example.lunchtimeboot.infrastructure.client.user.http;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockserver.client.MockServerClient;

import java.util.UUID;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockServerFixture {

    public void mockServerGetUser(String host, int port) throws JSONException {
        // first user
        UUID firstUserId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        JSONObject firstUserResponse = new JSONObject()
                .put("id", firstUserId.toString())
                .put("name", "tee.tan")
                .put("email", "tee.tan@email.com")
                .put("mobile", "0811111111");
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/user/api/users/" + firstUserId.toString())
                )
                .respond(
                        response()
                                .withBody(firstUserResponse.toString())
                );

        // 2nd user
        UUID secondUserId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        JSONObject secondUserResponse = new JSONObject()
                .put("id", secondUserId.toString())
                .put("name", "johari.sri")
                .put("email", "johari.sri@email.com")
                .put("mobile", "0822222222");
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/user/api/users/" + secondUserId.toString())
                )
                .respond(
                        response()
                                .withBody(secondUserResponse.toString())
                );
    }
}
