package am.ik.eget2.task.movie.download;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleDownloadMonitor extends AbstractDownloadMonitor {
    private final ConcurrentMap<String, DownloadWork> workingMap = new ConcurrentHashMap<String, DownloadWork>();

    private final ConcurrentMap<String, DownloadWork> finishedMap = new ConcurrentHashMap<String, DownloadWork>();

    @Override
    protected ConcurrentMap<String, DownloadWork> getWorkingMap() {
        return workingMap;
    }

    @Override
    protected ConcurrentMap<String, DownloadWork> getFinishedMap() {
        return finishedMap;
    }

}
