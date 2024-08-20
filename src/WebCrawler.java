import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class WebCrawler implements Runnable{
    private static final int MAX_DEPTH = 1;
    private Thread thread;
    private String firstLink;
    private final ArrayList<String> visitedLinks = new ArrayList<String>();
    private int ID;

    public WebCrawler(String link, int num) {
        System.out.print("WebCrawler created");
        firstLink = link;
        ID = num;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        crawl(1, firstLink);
    }

    private void crawl(int level, String url) {
        if(level <= MAX_DEPTH) {
            Document doc = request(url);

            if(doc != null) {
                for(Element link : doc.select("a[href]")) {
                    String nextLink = link.absUrl("href");
                    if(!visitedLinks.contains(nextLink)) {
                        crawl(level++, nextLink);
                    }
                }
            }
        }


    }

    private Document request(String url) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if(con.response().statusCode() == 200) {
                System.out.println(STR."\n**Bot ID: \{ID} Received Webpage at \{url}");
                System.out.println(doc.title());
                visitedLinks.add(url);

                return doc;
            }
            return null;

        } catch (IOException e) {
            return null;
        }
    }

    public Thread getThread() {
        return thread;
    }
}
