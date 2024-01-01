package toy.project.boot.domain.api.dto;

import lombok.Data;

@Data
public class DrugResponse {

    private String resultMsg;

    private String entpName;
    private String itemName;
    private String itemSeq;
    private String efcyQesitm;
    private String useMethodQesitm;
    private String atpnQesitm;

}
