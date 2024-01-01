package toy.project.boot.domain.api;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import toy.project.boot.domain.api.dto.ApiResponse;
import toy.project.boot.domain.api.dto.DrugResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Service
public class DrugService {
    public DrugResponse parseXml(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlString)));

            // XML의 header에서 원하는 값들을 가져옴
            NodeList msgHeaderList = document.getElementsByTagName("header");

            if (msgHeaderList.getLength() > 0) {
                Node msgHeader = msgHeaderList.item(0);
                if (msgHeader.getNodeType() == Node.ELEMENT_NODE) {
                    Element msgHeaderElement = (Element) msgHeader;

                    // msgHeader의 하위 요소들을 가져옴
                    String resultCode = getElementValue(msgHeaderElement, "resultCode");
                    String resultMsg = getElementValue(msgHeaderElement, "resultMsg");

                    // ApiResponse 객체에 값 설정
                    DrugResponse drugResponse = new DrugResponse();
                    drugResponse.setResultMsg(resultMsg);

                    // XML의 msgBody에서 busArrivalList를 가져옴
                    NodeList drugInfo = document.getElementsByTagName("body");

                    if (drugInfo.getLength() > 0) {
                        Node busArrival = drugInfo.item(0);
                        if (busArrival.getNodeType() == Node.ELEMENT_NODE) {
                            Element busArrivalElement = (Element) busArrival;

                            String itemName = getElementValue(busArrivalElement, "itemName");

                        }
                    }

                    return drugResponse;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrugResponse defaultResult = new DrugResponse();
        defaultResult.setResultMsg("오류가 발생했습니다.");
        return defaultResult;
    }


    private String getElementValue(Element parentElement, String elementName) {
        NodeList nodeList = parentElement.getElementsByTagName(elementName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }
}
