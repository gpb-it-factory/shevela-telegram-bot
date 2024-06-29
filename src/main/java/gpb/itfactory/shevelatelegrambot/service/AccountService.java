package gpb.itfactory.shevelatelegrambot.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gpb.itfactory.shevelatelegrambot.client.ClientManager;
import gpb.itfactory.shevelatelegrambot.dto.CreateAccountDto;
import gpb.itfactory.shevelatelegrambot.dto.ErrorDto;
import gpb.itfactory.shevelatelegrambot.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.util.List;

@Slf4j
@Component
public class AccountService {

    private final ClientManager<RestClient> restClientManager;

    public AccountService(ClientManager<RestClient> restClientManager) {
        this.restClientManager = restClientManager;
    }

    public String createUserAccountV2(Long tgUserId, CreateAccountDto createAccountDto) {
        log.info("Create request to MiddleService: < create account >");
        try {
            String uri ="/users/" + tgUserId + "/accounts";
            ResponseEntity<String> responseEntity = restClientManager.getClient()
                    .post().uri(uri).body(createAccountDto).retrieve().toEntity(String.class);
            log.info("Receive response from Middle Service: < create account >");
            if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
                return "Account has been successfully created";
            } else if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return "Account is already open. %s".formatted(buildSuccessResponseToGetUserAccountsV2(responseEntity));
            }
            return "Error << Unknown error >>";
        } catch (HttpServerErrorException exception) {
            log.info("Receive response from Middle Service: < create account > >  %s"
                    .formatted(exception.toString()));
            return handleHttpServerErrorException(exception);
        } catch (HttpClientErrorException exception) {
            log.info("Receive response from Middle Service: < create account >  %s"
                    .formatted(exception.toString()));
            return handleHttpClientErrorException(exception);
        } catch (RestClientException exception) {
            log.info("Receive response from Middle Service: < create account > RestClientException  %s"
                    .formatted(exception.toString()));
            return "Middle service unknown or client error: " + exception;
        }
    }

    public String getUserAccountsV2(Long tgUserId) {
        log.info("Create request to MiddleService: < get accounts >");
        try {
            String uri = "/users/" + tgUserId + "/accounts";
            ResponseEntity<String> responseEntity = restClientManager.getClient()
                    .get().uri(uri).retrieve().toEntity(String.class);
            log.info("Receive response from Middle Service: < get accounts %s: ".formatted(responseEntity.getBody()));
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return buildSuccessResponseToGetUserAccountsV2(responseEntity);
            }
            return "Error << Unknown error >>";
        } catch (HttpServerErrorException exception) {
            log.info("Receive response from Middle Service: < get user accounts > >  %s"
                    .formatted(exception.toString()));
            return handleHttpServerErrorException(exception);
        } catch (HttpClientErrorException exception) {
            log.info("Receive response from Middle Service: < get user accounts >  %s"
                    .formatted(exception.toString()));
            return handleHttpClientErrorException(exception);
        } catch (RestClientException exception) {
            log.info("Receive response from Middle Service: < get user accounts > RestClientException  %s"
                    .formatted(exception.toString()));
            return "Middle service unknown or client error: " + exception;
        }
    }

    private String buildSuccessResponseToGetUserAccountsV2(ResponseEntity<String> responseEntity) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Account> accounts = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
            log.info("Receive response from BackendService: < get accounts > - Accounts: %s"
                    .formatted(accounts.toString()));
            Account account = accounts.get(0);
            return "User has open account: %s, amount: %s".formatted(account.getAccountName(), account.getAmount());
        }  catch (JsonProcessingException exception) {
            log.info("Receive response from Middle Service: < get user account > JsonProcessingException  %s"
                    .formatted(exception.toString()));
            return "Error: " + exception;
        }
    }

    private String handleHttpServerErrorException(HttpServerErrorException exception) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ErrorDto errorDto = mapper.readValue(exception.getResponseBodyAs(String.class), ErrorDto.class);
            return switch (errorDto.getCode()) {
                case ("103") -> "Error << Internal Backend server error when verifying user registration >>";
                case ("113") -> "Error << Backend server unknown or client error when registration verification >>";
                case ("203") -> "Error << Internal Backend server error when account verification >>";
                case ("213") -> "Error << Backend server unknown or client error when account verification >>";
                case ("200") -> "Error << Internal Backend server error when create account >>";
                case ("210") -> "Error << Backend server unknown or client error when create account >>";
                default -> "Error << Unknown error >>";
            };
        } catch (JsonProcessingException e) {
            log.info("Receive response from Middle Service: < get user accounts > JsonProcessingException  %s"
                    .formatted(exception.toString()));
            return "Error: " + exception;
        }
    }

    private String handleHttpClientErrorException(HttpClientErrorException exception) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ErrorDto errorDto = mapper.readValue(exception.getResponseBodyAs(String.class), ErrorDto.class);
            if (errorDto.getCode().equals("101")) {
                return "User is not registered in the MiniBank";
            }
            return "Error << User does not have account in the MiniBank >>";
        } catch (JsonProcessingException e) {
            log.info("Receive response from Middle Service: < get user accounts > JsonProcessingException  %s"
                    .formatted(exception.toString()));
            return "Error: " + exception;
        }
    }
}
