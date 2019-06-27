package uk.ac.warwick;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class FetchNews {
    public static void main(String[] args) throws IOException {
//        System.out.println(getNews("https://tw.appledaily.com/new/realtime/20180101/1270306/"));
        System.out.println(parseNews(getNews("https://tw.appledaily.com/new/realtime/20180101/1270133/")));
//        System.out.println(getNews("https://tw.appledaily.com/new/realtime/20180101/1270300/"));
    }

    private static Document getNews(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    private static String parseNews(Document doc){
        Elements select = doc.select(".ndArticle_contentBox .ndArticle_margin");
        Elements iframe = select.select("iframe");
        iframe.nextAll().remove();
        iframe.remove();

        select = doc.select(".ndArticle_contentBox .ndArticle_margin p");
        return select.text();
    }
}
