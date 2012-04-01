package am.ik.eget2.bootstrap;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import am.ik.eget2.task.anypage.AnyPageTask;
import am.ik.eget2.task.movie.MovieTask;
import am.ik.eget2.task.specificpage.SpecificPageTask;

/**
 * Eget2サービスクラス
 *
 */
public class Eget2Service implements Runnable {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(Eget2Service.class);
    
    private AnyPageTask anyPageTask;
    private SpecificPageTask specificPageTask;
    private MovieTask movieTask;

    private int anyPageThreadCount = 1;
    private int specificPageThreadCount = 1;
    private int movieThreadCount = 6;

    public void setAnyPageTask(AnyPageTask anyPageTask) {
        this.anyPageTask = anyPageTask;
    }
    
    public void setSpecificPageTask(SpecificPageTask specificPageTask) {
        this.specificPageTask = specificPageTask;
    }
    
    public void setMovieTask(MovieTask movieTask) {
        this.movieTask = movieTask;
    }

    public void setAnyPageThreadCount(int anyPageThreadCount) {
        this.anyPageThreadCount = anyPageThreadCount;
    }

    public void setSpecificPageThreadCount(int specificPageThreadCount) {
        this.specificPageThreadCount = specificPageThreadCount;
    }

    public void setMovieThreadCount(int movieThreadCount) {
        this.movieThreadCount = movieThreadCount;
    }

    @Override
    public void run() {
        int poolSize = anyPageThreadCount + specificPageThreadCount
                + movieThreadCount;
        ScheduledExecutorService exec = Executors
                .newScheduledThreadPool(poolSize);
        // 任意ページタスクの開始
        for (int i = 0; i < anyPageThreadCount; i++) {
            LOGGER.info("start AnyPageTask[" + i + "]");
            exec.scheduleAtFixedRate(anyPageTask, 0, 1, TimeUnit.SECONDS);
        }
        // 特定ページタスクの開始
        for (int i = 0; i < specificPageThreadCount; i++) {
            LOGGER.info("start SpecificPageTask[" + i + "]");
            exec.scheduleAtFixedRate(specificPageTask, 0, 1, TimeUnit.SECONDS);
        }
        // 動画取得タスクの開始
        for (int i = 0; i < movieThreadCount; i++) {
            LOGGER.info("start MovieTask[" + i + "]");
            exec.scheduleAtFixedRate(movieTask, 0, 1, TimeUnit.SECONDS);
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exec.shutdown();
        LOGGER.info("finished!");
    }
}
