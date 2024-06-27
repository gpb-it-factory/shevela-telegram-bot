package gpb.itfactory.shevelatelegrambot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gpb.itfactory.shevelatelegrambot.bot.util.ClientManager;
import gpb.itfactory.shevelatelegrambot.dto.ErrorDto;
import gpb.itfactory.shevelatelegrambot.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

@Slf4j
@Component
public class UserService {

    private final ClientManager<RestClient> restClientManager;

    public UserService(ClientManager<RestClient> restClientManager) {
        this.restClientManager = restClientManager;
    }

    public String createUserV2(UserDto userDto) {
        log.info("Create request to MiddleService: < register new user >");
        try {
            ResponseEntity<String> responseEntity = restClientManager.getClient()
                    .post().uri("/users").body(userDto).retrieve().toEntity(String.class);
            log.info("Receive response from Middle Service: < register new user > %s: ".formatted(responseEntity.getBody()));
            if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
                return "User %s has been successfully registered in the MiniBank".formatted(userDto.getUsername());
            } else if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return "User already exists in the MiniBank";
            }
            return responseEntity.getBody();
        } catch (HttpServerErrorException exception) {
            log.info("Receive response from Middle Service: < create user > >  %s"
                    .formatted(exception.toString()));
            return handleHttpServerErrorException(exception);
        } catch (RestClientException exception) {
            log.info("Receive response from Middle Service: < create user > RestClientException  %s"
                    .formatted(exception.toString()));
            return "Middle service unknown or connection error: " + exception;
        }
    }

    public String getUserByTelegramIdV2(long tgUserId) {
        log.info("Create request to MiddleService: < get user by userId >");
        try {
            ResponseEntity<String> responseEntity = restClientManager.getClient()
                    .get().uri("/users/"  + tgUserId).retrieve().toEntity(String.class);
            log.info("Receive response from Middle Service: < get user by userId > %s: ".formatted(responseEntity.getBody()));
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return "User is registered in the MiniBank";
            }
            return responseEntity.getBody();
        } catch (HttpServerErrorException exception) {
            log.info("Receive response from Middle Service: < get user by userId > >  %s"
                    .formatted(exception.toString()));
            return handleHttpServerErrorException(exception);
        } catch (HttpClientErrorException exception) {
            log.info("Receive response from Middle Service: < get user by tgUserId >  %s"
                    .formatted(exception.toString()));
            return "User is not registered in the MiniBank";
        } catch (RestClientException exception) {
            log.info("Receive response from Middle Service: < get user by tgUserId > RestClientException  %s"
                    .formatted(exception.toString()));
            return "Middle service unknown or connection error: " + exception;
        }
    }

    private String handleHttpServerErrorException(HttpServerErrorException exception) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ErrorDto errorDto = mapper.readValue(exception.getResponseBodyAs(String.class), ErrorDto.class);
            return switch (errorDto.getCode()) {
                case ("103") -> "Error << Internal Backend server error when verifying user registration >>";
                case ("100") -> "Error << Internal Backend server error when create User >>";
                case ("113") -> "Error << Backend server unknown or connection error when registration verification >>";
                case ("110") -> "Error << Backend server unknown or connection error when create user >>";
                default -> "Error << Unknown error >>";
            };
        } catch (JsonProcessingException e) {
            log.info("Receive response from Middle Service: < get user accounts > JsonProcessingException  %s"
                    .formatted(exception.toString()));
            return "Error: " + exception;
        }
    }
}
