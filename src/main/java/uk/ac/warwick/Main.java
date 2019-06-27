package uk.ac.warwick;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.text.ParseException;
import java.util.Iterator;

import static uk.ac.warwick.DBUtils.getDataSource;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = getDataSource();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        int year = 2017;
        for (int month = 1; month <= 12; month++) {
            fetchNewsList(namedParameterJdbcTemplate, year, month);
        }
    }

    public static void fetchNewsList(NamedParameterJdbcTemplate namedParameterJdbcTemplate, int year, int month) {
        for (int day = 1; day <= DateUtils.getEndDateOfMonth(year, month); day++) {
            for (int page = 1; ; page++) {
                try {
                    System.out.println("[" + year + ", " + month + ", " + day + ", " + page + "]...");
                    ArrayNode newsList = FetchNewsList.get(year + "/" + month + "/" + day, page);
                    if (newsList.size() == 0) {
                        System.out.println("The size of page " + page + " is zero");
                        break;
                    }
                    insertNewsList(namedParameterJdbcTemplate, newsList);
                } catch (Exception ex) {
                    System.out.println("[" + year + ", " + month + ", " + day + " , " + page + "] doesn't work well...");
                    System.out.println(ex);
                }
            }
        }
    }

    public static void insertNewsList(NamedParameterJdbcTemplate namedParameterJdbcTemplate, ArrayNode newsList) throws ParseException {
        Iterator iterator = newsList.iterator();
        int n = newsList.size();
        News[] newsArr = new News[n];
        int i = 0;
        while (iterator.hasNext()) {
            News news = new News();
            ObjectNode objectNode = (ObjectNode) iterator.next();
            news.setTimestamp(DateUtils.parseToTimeStamp(objectNode.get("pubDate").asText()));
            news.setUrl(objectNode.get("sharing").get("url").asText());
            news.setTitle(objectNode.get("title").asText());
            newsArr[i] = news;
            i++;
        }
        DBUtils.insertNews(namedParameterJdbcTemplate, newsArr);
    }
}
