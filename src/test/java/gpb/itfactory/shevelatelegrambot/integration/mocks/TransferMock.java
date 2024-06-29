package gpb.itfactory.shevelatelegrambot.integration.mocks;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class TransferMock {

    public static void setupCreateTransferResponseSuccess(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
//                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"message\": \"Transfer has been successfully completed\"}")));
    }

    public static void setupCreateTransferV2ResponseIfBalanceLessAmount(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withBody(createErrorJSON("Error << Not enough money in the account >>",
                                "createTransferV2Error", "311"))));
    }

    public static void setupCreateTransferV2ResponseIfUserSenderIsNotPresent(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody(createErrorJSON("User is not registered in the MiniBank",
                                "getUserError", "101"))));
    }

    public static void setupCreateTransferV2ResponseIfUserRecipientIsNotPresent(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody(createErrorJSON("User is not registered in the MiniBank",
                                "getUserError", "102"))));
    }


    public static void setupCreateTransferIfUserSenderAccountIsNotPresent(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody(createErrorJSON("User does not have account",
                                "getUserAccountsError", "201"))));
    }

    public static void setupCreateTransferResponseFail(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody(createErrorJSON("Create transfer internal server error",
                                "createTransferV2Error", "300"))));
    }

    public static void setupCreateTransferResponseIfBackendServerNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                        .withBody(createErrorJSON(
                                "Backend server unknown or client error when create transfer",
                                "createTransferV2Error", "310"))));
    }

    public static void setupCreateTransferResponseIfAccountToTransferIsNotPresent(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody(createErrorJSON("Account for the transfer was not found",
                                "createTransferV2Error", "301"))));
    }


    public static void setupCreateTransferResponseNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));
    }


    public static void setupCreateTransferResponseIfGetUserByTelegramIdResponseFail(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody(createErrorJSON("Registration verification internal server error",
                                "getUserError", "103"))));
    }

    public static void setupCreateTransferResponseIfGetUserByTelegramIdRequestNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                        .withBody(createErrorJSON("Backend server unknown" + " or client error" +
                                " when registration verification", "getUserError", "113"))));
    }

    public static void setupCreateTransferResponseIfGetAccountsResponseServerError(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody(createErrorJSON("Error receiving accounts",
                                "getUserAccountsError", "203"))));
    }

    public static void setupCreateTransferResponseIfGetAccountsResponseNoConnection(WireMockServer mockService) {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/v2/middle/transfers"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                        .withBody(createErrorJSON("Backend server unknown" + " or client error" +
                                " when user accounts verification", "getUserAccountsError", "213"))));
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
