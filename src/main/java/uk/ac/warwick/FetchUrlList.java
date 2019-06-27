package uk.ac.warwick;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class FetchUrlList {
    public static void main(String[] args){
        RestTemplate restTemplate = new RestTemplate();
        String url = getUrlBase(1, 2018, 1).build().toString();
        ResponseEntity<ArrayNode> forEntity = restTemplate.getForEntity(url, ArrayNode.class);
        System.out.println(forEntity.getBody());
    }

    private static UriComponentsBuilder getUrlBase(int page, int year, int month) {
        String APPLE_DAILY_URL = "https://tw.appledaily.com/search/ajaxresult";
        String monthStr = month <10 ? "0"+month: String.valueOf(month);
        String date = year+"/"+month+"/"+monthStr;

        return UriComponentsBuilder.fromHttpUrl(APPLE_DAILY_URL)
                .queryParam("querystrS", "說")
                .queryParam("sort", "time")
                .queryParam("searchType", "all")
                .queryParam("dateStart", date)
                .queryParam("dateEnd", date)
                .queryParam("page", page);
    }
}
