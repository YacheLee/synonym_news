package uk.ac.warwick;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class FetchNewsList {
    static final String APPLE_DAILY_URL = "https://tw.appledaily.com/search/ajaxresult";
    public static ArrayNode get(int year, int month, int page){

        String dateStart = DateUtils.getStartDate(year, month);
        String dateEnd = DateUtils.getEndDate(year, month);

        RestTemplate restTemplate = new RestTemplate();
        String url = getUrlBase(page, dateStart, dateEnd).build().toString();
        return restTemplate.getForEntity(url, ArrayNode.class).getBody();
    }

    private static UriComponentsBuilder getUrlBase(int page, String dateStart, String dateEnd) {
        return UriComponentsBuilder.fromHttpUrl(APPLE_DAILY_URL)
                .queryParam("querystrS", "1=1")
                .queryParam("sort", "time")
                .queryParam("searchType", "all")
                .queryParam("dateStart", dateStart)
                .queryParam("dateEnd", dateEnd)
                .queryParam("page", page);
    }
}
