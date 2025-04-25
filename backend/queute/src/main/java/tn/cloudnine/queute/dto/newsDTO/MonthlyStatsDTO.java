package tn.cloudnine.queute.dto.newsDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MonthlyStatsDTO {
    private String month;
    private String year;
    private Long totalNews;
    private Long totalActions;
}
