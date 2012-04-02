package am.ik.eget2.task.movie.download;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import am.ik.eget2.task.movie.Movie;
import am.ik.eget2.task.movie.download.DownloadWork.Status;

abstract public class AbstractDownloadMonitor implements DownloadMonitor {
    abstract protected ConcurrentMap<String, DownloadWork> getWorkingMap();

    abstract protected ConcurrentMap<String, DownloadWork> getFinishedMap();

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<DownloadWork> getWorkingList() {
        return new CopyOnWriteArrayList<DownloadWork>(getWorkingMap().values());
    }

    @Override
    public List<DownloadWork> getFinishedList() {
        return new CopyOnWriteArrayList<DownloadWork>(getFinishedMap().values());
    }

    @Override
    public void start(Movie movie) {
        String key = movie.getMovieUrl();
        DownloadWork work = new DownloadWork();
        work.setMovie(movie);
        work.setBegin(new Date());
        work.setStatus(Status.WORKING);
        logger.trace("started {}", work);
        getWorkingMap().put(key, work);
    }

    private void moveToFinishd(Movie movie, Status status) {
        String key = movie.getMovieUrl();
        DownloadWork work = getWorkingMap().get(key);
        if (work != null) {
            getWorkingMap().remove(key);
        } else {
            work = new DownloadWork();
            work.setMovie(movie);
        }
        work.setEnd(new Date());
        work.setStatus(status);
        logger.trace("finished {}", work);
        getFinishedMap().put(key, work);
    }

    @Override
    public void success(Movie movie) {
        moveToFinishd(movie, Status.SUCCESSED);
    }

    @Override
    public void fail(Movie movie) {
        moveToFinishd(movie, Status.FAILED);
    }

}
