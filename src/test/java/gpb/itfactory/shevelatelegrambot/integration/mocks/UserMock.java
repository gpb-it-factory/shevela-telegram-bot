package gpb.itfactory.shevelatelegrambot.integration.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class UserMock {

    private final static String USER_NAME = "test";
    private final static Long USER_ID = 123456L;

    public static void setupCreateUserResponseSuccess(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users"))
                .willReturn(WireMock.aResponse()
                        .withBody("User %s has been successfully registered".formatted(USER_NAME))));
    }
    public static void setupCreateUserResponseFail(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users"))
                .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }
    public static void setupGetUserByTelegramIdResponseSuccess(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID))
                .willReturn(WireMock.aResponse()
                        .withBody("User %s is registered".formatted(USER_NAME))));
    }
    public static void setupGetUserByTelegramIdResponseFail(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID))
                .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }
}
