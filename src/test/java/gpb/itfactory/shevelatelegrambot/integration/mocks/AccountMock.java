package gpb.itfactory.shevelatelegrambot.integration.mocks;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;

public class AccountMock {

    private final static String ACCOUNT_NAME = "test";
    private final static Long USER_ID = 123456L;

    public static void setupCreateUserAccountResponseSuccess(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withBody("Account %s has been successfully created".formatted(ACCOUNT_NAME))));
    }
    public static void setupCreateUserAccountResponseFail(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                ));
    }
    public static void setupGetUserAccountsResponseSuccess(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withBody("User has open account")));
    }
    public static void setupGetUserAccountsResponseFail(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                ));
    }
}
