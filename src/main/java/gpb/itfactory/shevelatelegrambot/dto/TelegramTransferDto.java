package gpb.itfactory.shevelatelegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TelegramTransferDto {
    String from;
    long fromId;
    String to;
    float ammount;
}
