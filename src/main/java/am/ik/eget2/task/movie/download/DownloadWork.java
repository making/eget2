package am.ik.eget2.task.movie.download;

import java.io.Serializable;
import java.util.Date;

import am.ik.eget2.task.movie.Movie;

public class DownloadWork implements Serializable {
    private static final long serialVersionUID = 8579268472641317682L;

    public static enum Status {
        WORKING, SUCCESSED, FAILED
    }

    private Movie movie;
    private Status status;
    private Date begin;
    private Date end;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "DownloadWork [movie=" + movie + ", status=" + status
                + ", begin=" + begin + ", end=" + end + "]";
    }
}
