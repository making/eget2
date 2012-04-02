package am.ik.eget2.hazelcast;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.InitializingBean;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import am.ik.eget2.task.movie.download.AbstractDownloadMonitor;
import am.ik.eget2.task.movie.download.DownloadWork;

public class HazelcastDownloadMonitor extends AbstractDownloadMonitor implements
        InitializingBean {
    private final IMap<String, DownloadWork> workingMap;
    private final IMap<String, DownloadWork> finishedMap;
    private boolean clearOnInit = false;

    public HazelcastDownloadMonitor(String workingMapName,
            String finishedMapName, HazelcastInstance instance) {
        workingMap = instance.getMap(workingMapName);
        finishedMap = instance.getMap(finishedMapName);
    }

    public void setClearOnInit(boolean clearOnInit) {
        this.clearOnInit = clearOnInit;
    }

    @Override
    protected ConcurrentMap<String, DownloadWork> getWorkingMap() {
        return workingMap;
    }

    @Override
    protected ConcurrentMap<String, DownloadWork> getFinishedMap() {
        return finishedMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (clearOnInit) {
            finishedMap.clear();
        }
    }
}
