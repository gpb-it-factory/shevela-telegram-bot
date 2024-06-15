package gpb.itfactory.shevelatelegrambot.service;


import gpb.itfactory.shevelatelegrambot.dto.CreateAccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class AccountService {
    @Value("${middle.service.url}")
    private String BASE_URL;

    private final RestTemplate restTemplate;

    public AccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createUserAccountV2(Long tgUserId, CreateAccountDto createAccountDto){
        String exception;
        log.info("Create request to MiddleService: < create account >");
        try {
            String url = BASE_URL + "/" + tgUserId + "/accounts";
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, createAccountDto, String.class);
            log.info("Receive response from Middle Service: < create account >");
            return responseEntity.getBody();
        } catch (RestClientException restClientException) {
            exception = restClientException.toString();
            log.error(exception);
        }
        log.error("Error: " + exception);
        return "Error: " + exception;
    }

    public String getUserAccountsV2(Long tgUserId){
        String exception;
        log.info("Create request to MiddleService: < get accounts >");
        try {
            String url = BASE_URL + "/" + tgUserId + "/accounts";
            String response = restTemplate.getForObject(url, String.class);
            log.info("Receive response from Middle Service: < get accounts >");
            return response;
        } catch (RestClientException restClientException) {
            exception = restClientException.toString();
            log.error(exception);
        }
        log.error("Error: " + exception);
        return "Error: " + exception;
    }
}
