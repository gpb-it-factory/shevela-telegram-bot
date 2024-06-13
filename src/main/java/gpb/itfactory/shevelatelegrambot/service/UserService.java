package gpb.itfactory.shevelatelegrambot.service;

import gpb.itfactory.shevelatelegrambot.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class UserService {

    @Value("${middle.service.url}")
    private String URL;

    private final RestTemplate restTemplate;
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createUser(UserDto userDto){
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

    public String findUserByUsername(String username){
        String exception;
        log.info("Create request to MiddleService: < find user by username >");
        try {
            String response = restTemplate.getForObject(URL + "/" + username, String.class);
            log.info("Receive response from Middle Service: < find user by username >");
            return response;
        } catch (RestClientException restClientException) {
            exception = restClientException.toString();
            log.error(exception);
        }
        log.error("Error: " + exception);
        return "Error: " + exception;
    }
}
