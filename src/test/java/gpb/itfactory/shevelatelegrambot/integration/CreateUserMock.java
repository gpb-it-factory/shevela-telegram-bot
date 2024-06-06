package gpb.itfactory.shevelatelegrambot.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class CreateUserMock {

    private final static String USER_NAME = "test";

    public static void setupCreateUserResponseSuccess(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/middle/users"))
                .willReturn(WireMock.aResponse()
                        .withBody("User %s has been successfully registered".formatted(USER_NAME))));
    }
    public static void setupCreateUserResponseFail(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/middle/users"))
                .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }
    public static void setupFindUserByUsernameResponseSuccess(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/middle/users/" + USER_NAME))
                .willReturn(WireMock.aResponse()
                        .withBody("User %s is registered".formatted(USER_NAME))));
    }
    public static void setupFindUserByUsernameResponseFail(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/middle/users/" + USER_NAME))
                .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }
}
