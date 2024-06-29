package gpb.itfactory.shevelatelegrambot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    private String accountName;
    private String accountId;
    private Float amount;
}
