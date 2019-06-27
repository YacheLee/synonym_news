package uk.ac.warwick;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class FetchNewsList {
    public static ArrayNode get(int year, int month, int page){
        RestTemplate restTemplate = new RestTemplate();
        String url = getUrlBase(year, month, page).build().toString();
        return restTemplate.getForEntity(url, ArrayNode.class).getBody();
    }

    private static UriComponentsBuilder getUrlBase(int year, int month, int page) {
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
