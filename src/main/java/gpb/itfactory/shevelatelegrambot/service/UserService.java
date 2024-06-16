package gpb.itfactory.shevelatelegrambot.service;

import gpb.itfactory.shevelatelegrambot.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@PropertySource("classpath:application.yml")
public class UserService {

    @Value("${middle.service.url}")
    private String URL;

    private final RestTemplate restTemplate;
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createUserV2(UserDto userDto){
        String exception;
        log.info("Create request to MiddleService: < register new user >");
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, userDto, String.class);
            log.info("Receive response from Middle Service: < register new user >");
            return responseEntity.getBody();
        } catch (RestClientException restClientException) {
            exception = restClientException.toString();
            log.error(exception);
        }
        log.error("Error: " + exception);
        return "Error: " + exception;
    }

    public String getUserByTelegramIdV2(Long tgUserId){
        String exception;
        log.info("Create request to MiddleService: < get user by userId >");
        try {
            String response = restTemplate.getForObject(URL + "/" + tgUserId, String.class);
            log.info("Receive response from Middle Service: < get user by userId >");
            return response;
        } catch (RestClientException restClientException) {
            exception = restClientException.toString();
            log.error(exception);
        }
        log.error("Error: " + exception);
        return "Error: " + exception;
    }
}
