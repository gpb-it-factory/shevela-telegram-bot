package gpb.itfactory.shevelatelegrambot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gpb.itfactory.shevelatelegrambot.client.ClientManager;
import gpb.itfactory.shevelatelegrambot.dto.ErrorDto;
import gpb.itfactory.shevelatelegrambot.dto.TelegramTransferDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
public class TransferService {

    private final ClientManager<RestClient> restClientManager;

    public TransferService(ClientManager<RestClient> restClientManager) {
        this.restClientManager = restClientManager;
    }

    public String createTransferV2(TelegramTransferDto telegramTransferDto) {
        log.info("Create request to MiddleService: < create transfer >");
        try {
            ResponseEntity<String> responseEntity = restClientManager.getClient()
                    .post().uri("/transfers").body(telegramTransferDto).retrieve().toEntity(String.class);
            log.info("Receive response from Middle Service: < create transfer >");
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return "Transfer has been successfully completed";
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

    private String handleHttpServerErrorException(HttpServerErrorException exception) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ErrorDto errorDto = mapper.readValue(exception.getResponseBodyAs(String.class), ErrorDto.class);
            return switch (errorDto.getCode()) {
                case ("103") -> "Error << Internal Backend server error when verifying user registration >>";
                case ("113") -> "Error << Backend server unknown or client error when users registration verification >>";
                case ("203") -> "Error << Internal Backend server error when sender account verification >>";
                case ("213") -> "Error << Backend server unknown or client error when sender account verification >>";
                case ("300") -> "Error << Internal Backend server error when create transfer >>";
                case ("310") -> "Error << Backend server unknown or client error when create transfer >>";
                case ("301") -> "Error << Account for the transfer was not found >>";
                default -> "Error << Unknown server error >>";
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
            return switch (errorDto.getCode()) {
                case ("101") -> "Error << User sender is not registered in the MiniBank >>";
                case ("201") -> "Error << User sender does not have account in the MiniBank >>";
                case ("102") -> "Error << User recipient is not registered in the MiniBank >>";
                case ("311") -> "Error << User sender does not have enough money in the account >>";
                default -> "Error << Unknown client error >>";
            };
        } catch (JsonProcessingException e) {
            log.info("Receive response from Middle Service: < get user accounts > JsonProcessingException  %s"
                    .formatted(exception.toString()));
            return "Error: " + exception;
        }
    }

}
