package am.ik.eget2.task.specificpage;

import java.net.SocketTimeoutException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import am.ik.eget2.queue.TaskQueue;
import am.ik.eget2.task.movie.Movie;

public class SpecificPageTask implements Runnable, InitializingBean {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SpecificPageTask.class);

    private TaskQueue<SpecificPage> specificPageQueue;

    private TaskQueue<Movie> movieQueue;

    private String moviePageUrlPrefix;

    public void setSpecificPageQueue(TaskQueue<SpecificPage> specificPageQueue) {
        this.specificPageQueue = specificPageQueue;
    }

    public void setMovieQueue(TaskQueue<Movie> movieQueue) {
        this.movieQueue = movieQueue;
    }

    public void setMoviePageUrlPrefix(String moviePageUrlPrefix) {
        this.moviePageUrlPrefix = moviePageUrlPrefix;
    }

    @Override
    public void run() {
        LOGGER.trace("begin specific page task");
        SpecificPage page = specificPageQueue.pull();
        LOGGER.trace("pull specific page");
        if (page != null) {
            String url = page.getUrl();
            try {
                // 特定ページにアクセス
                Document doc = Jsoup.connect(url).get();
                String title = doc.select("h1").text();
                LOGGER.trace("retrieve {}", title);
                // 動画リンクを取得
                Elements as = doc.select("a[href^=" + moviePageUrlPrefix + "]");
                for (Element a : as) {
                    String href = a.attr("href");
                    Movie movie = new Movie();
                    movie.setPageUrl(url);
                    movie.setTitle(title);
                    movie.setMovieUrl(href);
                    movieQueue.push(movie);
                }
            } catch (SocketTimeoutException e) {
                LOGGER.warn("retry page=" + url);
                // キューに追加してリトライ
                specificPageQueue.push(page);
            } catch (Exception e) {
                LOGGER.error("failed @" + url, e);
            }
        }
        LOGGER.trace("end specific page task");
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(specificPageQueue);
        Assert.notNull(movieQueue);
        Assert.notNull(moviePageUrlPrefix);
    };
}
