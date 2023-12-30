package toy.project.boot.domain.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.project.boot.domain.api.dto.ApiResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@RestController
public class ApiController {

    String serviceKey = "DOetYvMMk3ju0lVWP%2FlOog8gAchGS4YL6riswLmVJBMnGtps%2FQqdri3jW2KcUlra0XIH3JGRHnCwF7egRhmjfQ%3D%3D";
    @GetMapping("/api")
    public ApiResponse getData() {
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("stationId", "UTF-8") + "=" + URLEncoder.encode("200000078", "UTF-8")); /*정류소ID*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;

            ApiResponse apiResponse = new ApiResponse();
            while ((line = rd.readLine()) != null) {
                sb.append(line);
                System.out.println(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
