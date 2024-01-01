package toy.project.boot.domain.api.dto;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class ApiResponse {
    private String resultCode;
    private String resultMessage;
    private String queryTime;
    private String errorCode;


    private String predictTime1;
    private String remainSeatCnt1;
}
