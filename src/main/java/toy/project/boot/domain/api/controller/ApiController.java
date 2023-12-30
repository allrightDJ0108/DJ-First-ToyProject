package toy.project.boot.domain.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import toy.project.boot.domain.api.dto.ApiResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

@RestController
public class ApiController {

    @Value("${apiServiceKey}")
    private String apiSecretKey;

    @GetMapping("/api")
    public ApiResponse getData() {

        String apiUrl = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList";
        String stationId = "200000078";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("serviceKey", apiSecretKey)
                .queryParam("stationId", stationId);

        String url = builder.toUriString()+"&_type=json";

        System.out.println(url);

        // API 호출 및 응답 처리
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        System.out.println(result);

        ApiResponse apiResponse = parseXml(result);

        return apiResponse;
    }

    private ApiResponse parseXml(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlString)));

            // XML의 msgBody에서 busArrivalList를 가져옴
            NodeList busArrivalList = document.getElementsByTagName("busArrivalList");

            if (busArrivalList.getLength() > 0) {
                Node busArrival = busArrivalList.item(0);
                if (busArrival.getNodeType() == Node.ELEMENT_NODE) {
                    Element busArrivalElement = (Element) busArrival;

                    // 예측 도착 시간과 남은 좌석 수를 가져옴
                    String predictTime1 = getElementValue(busArrivalElement, "predictTime1");
                    String remainSeatCnt1 = getElementValue(busArrivalElement, "remainSeatCnt1");

                    // ApiResponse 객체에 값 설정
                    ApiResponse apiResponse = new ApiResponse();
                    apiResponse.setPredictTime1(predictTime1);
                    apiResponse.setRemainSeatCnt1(remainSeatCnt1);

                    return apiResponse;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getElementValue(Element parentElement, String elementName) {
        NodeList nodeList = parentElement.getElementsByTagName(elementName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }
}
