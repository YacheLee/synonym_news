package uk.ac.warwick;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.sql.DataSource;
import java.text.ParseException;
import java.util.Iterator;

import static uk.ac.warwick.DBUtils.getDataSource;

public class Main {
    public static void main(String[] args) throws ParseException {
        DataSource dataSource = getDataSource();
        ArrayNode newsList = FetchNewsList.get(2018, 1, 1);
        Iterator iterator = newsList.iterator();

        int n = newsList.size();
        News[] newsArr = new News[n];
        int i = 0;
        while(iterator.hasNext()){
            News news = new News();
            ObjectNode objectNode = (ObjectNode)iterator.next();
            news.setTimestamp(DateUtils.parseToTimeStamp(objectNode.get("pubDate").asText()));
            news.setUrl(objectNode.get("sharing").get("url").asText());
            
            newsArr[i] = news;
            i++;
        }

        DBUtils.insertNews(dataSource, newsArr);
    }
}
