package tn.cloudnine.queute.dto.forum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
public class PostCountDTO {
    private String month;
    private Long count;
    public PostCountDTO(String month, Long count) {
        this.month = month;
        this.count = count;
    }
}
