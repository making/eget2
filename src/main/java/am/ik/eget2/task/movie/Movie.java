package am.ik.eget2.task.movie;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Movie implements Serializable {
    private static final long serialVersionUID = 2362409884137641839L;
    private String pageUrl;
    private String title;
    private String movieUrl;
    private final AtomicInteger failedNum = new AtomicInteger(0);

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public AtomicInteger getFailedNum() {
        return failedNum;
    }

    @Override
    public String toString() {
        return "Movie [title=" + title + ", pageUrl=" + pageUrl + ", movieUrl="
                + movieUrl + ", failedNum=" + failedNum + "]";
    }

}
