package am.ik.eget2.task.movie.download;

import java.util.List;

import am.ik.eget2.task.movie.Movie;

public interface DownloadMonitor {
    List<DownloadWork> getWorkingList();

    List<DownloadWork> getFinishedList();

    void start(Movie movie);

    void success(Movie movie);

    void fail(Movie movie);
}
