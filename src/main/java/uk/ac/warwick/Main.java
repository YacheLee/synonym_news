package uk.ac.warwick;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.sql.DataSource;
import java.text.ParseException;
import java.util.Iterator;

import static uk.ac.warwick.DBUtils.getDataSource;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = getDataSource();
        int year = 2018, month = 1;

        for (int page = 1; ; page++) {
            try{
                System.out.println("["+year+","+month+", "+page+"]...");
                ArrayNode newsList = FetchNewsList.get(year, month, page);
                if(newsList.size()==0){
                    System.out.println("The size of page "+page+" is zero");
                    break;
                }
                insertNewsList(dataSource, newsList);
            }
            catch (Exception ex){
                System.out.println("["+year+","+month+", "+page+"] doesn't work well...");
                System.out.println(ex);
            }
        }
    }

    public static void insertNewsList(DataSource dataSource, ArrayNode newsList) throws ParseException {
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
        DBUtils.insertNews(dataSource, newsArr);
    }
}
