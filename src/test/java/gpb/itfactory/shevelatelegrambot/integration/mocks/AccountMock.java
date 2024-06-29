package gpb.itfactory.shevelatelegrambot.integration.mocks;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class AccountMock {

    private final static Long USER_ID = 123456L;

    public static void setupCreateUserAccountResponseSuccess(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withBody("{\"message\": \"Account has been successfully created\"}")));
    }

    public static void setupCreateUserAccountsV2ResponseIfAccountIsPresent(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(createAccountJSON())));
    }

    public static void setupCreateUserAccountsV2ResponseIfUserIsNotPresent(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody(createErrorJSON("User is not registered in the MiniBank",
                                "getUserError", "101"))));
    }

    public static void setupCreateUserAccountResponseFail(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody(createErrorJSON("Create user account error",
                                "createUserAccountError", "200"))));
    }

    public static void setupCreateUserAccountResponseNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/"  + USER_ID  + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));
    }

    public static void setupCreateUserAccountResponseIfBackendServerNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                        .withBody(createErrorJSON("Backend server unknown" +
                                        " or client error when create account",
                                "createUserAccountError", "210"))));
    }

    public static void setupCreateUserAccountResponseIfGetUserByTelegramIdResponseFail(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID  + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody(createErrorJSON("Registration verification internal server error",
                                "getUserError", "103"))));
    }

    public static void setupCreateUserAccountResponseIfGetUserByTelegramIdRequestNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID  + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                        .withBody(createErrorJSON("Backend server unknown" +
                                        " or client error when registration verification",
                                "getUserError", "113"))));
    }

    public static void setupCreateUserAccountResponseIfGetAccountsResponseServerError(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID  + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody(createErrorJSON("Error receiving accounts",
                                "getUserAccountsError", "203"))));
    }

    public static void setupCreateUserAccountResponseIfGetAccountsResponseNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID  + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                        .withBody(createErrorJSON("Backend server unknown" +
                                        " or client error when user accounts verification",
                                "getUserAccountsError", "213"))));
    }

    public static void setupGetUserAccountsResponseSuccess(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(createAccountJSON())));
    }

    public static void setupGetUserAccountsResponseIfNoAccount(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody(createErrorJSON("User does not have account",
                                "getUserAccountsError", "201"))));
    }

    public static void setupGetUserAccountsResponseIfUserNotPresent(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody(createErrorJSON("User is not registered in the MiniBank",
                                "getUserError", "101"))));
    }

    public static void setupGetUserAccountsResponseIfServerError(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody(createErrorJSON("Error receiving accounts",
                                "getUserAccountsError", "203"))));
    }

    public static void setupGetUserAccountsResponseIfNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/"  + USER_ID  + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));
    }

    public static void setupGetUserAccountResponseIfGetAccountsResponseNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID  + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                        .withBody(createErrorJSON("Backend server unknown or client error" +
                                " when user accounts verification", "getUserAccountsError", "213"))));
    }

    public static void setupGetUserAccountResponseIfGetUserByTelegramIdResponseFail(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID  + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody(createErrorJSON("Registration verification internal server error",
                                "getUserError", "103"))));
    }

    public static void setupGetUserAccountResponseIfGetUserByTelegramIdRequestNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/v2/middle/users/" + USER_ID  + "/accounts"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                        .withBody(createErrorJSON("Backend server unknown or client error" +
                                " when registration verification", "getUserError", "113"))));
    }

    private static String createAccountJSON() {
        return """
                [
                    {
                    \"accountId\": \"%s\",
                    \"accountName\": \"test\",
                    \"amount\": \"5000\"
                    }
                ]
                """.formatted(UUID.randomUUID().toString());
    }

    private static String createErrorJSON(String message, String type, String code) {

        return """ 
                {
                \"message\": \"%s\",
                \"type\": \"%s\",
                \"code\": \"%s\",
                \"trace_id\": \"%s\"
                }
                """.formatted(message, type, code, UUID.randomUUID());
    }

}
