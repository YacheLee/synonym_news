package uk.ac.warwick;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.nodes.Document;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static uk.ac.warwick.DBUtils.getDataSource;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = getDataSource();
        int year = 2018;
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        updateDatabaseHtml(namedParameterJdbcTemplate, year);
    }

    private static void updateDatabaseHtml(NamedParameterJdbcTemplate namedParameterJdbcTemplate, int year){
        List<Map<String, Object>> newsList = fetchNewsFromDatabase(namedParameterJdbcTemplate, year);
        int n = newsList.size();
        int i = 0;

        for(Map map: newsList){
            System.out.println((++i)+"/"+n);
            UUID id = (UUID)map.get("id");
            String url = (String)map.get("url");
            try {
                Document doc = FetchNews.getDoc(url);
                String html = FetchNews.getHtml(doc);
                updateNewsHtml(namedParameterJdbcTemplate, id, html);
            } catch (IOException e) {
                System.out.println("ERROR: ["+year+"], "+id);
                e.printStackTrace();
            }
        }
    }

    private static int updateNewsHtml(NamedParameterJdbcTemplate namedParameterJdbcTemplate, UUID id, String html){
        String sql = "UPDATE AppleDaily SET html=:html WHERE id = :id";
        HashMap<String, Object> map = new HashMap();
        map.put("id", id);
        map.put("html", html);
        return namedParameterJdbcTemplate.update(sql, map);
    }

    private static List<Map<String, Object>> fetchNewsFromDatabase(NamedParameterJdbcTemplate namedParameterJdbcTemplate, int year){
        String sql = "select id, url from appledaily\n" +
                "WHERE timestamp >= '"+year+"-01-01'\n" +
                "AND timestamp < '"+year+"-12-31'";
        Map<String, News> map = new HashMap();
        return namedParameterJdbcTemplate.queryForList(sql, map);
    }

    private static void fetchNewsList(NamedParameterJdbcTemplate namedParameterJdbcTemplate, int year){
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
