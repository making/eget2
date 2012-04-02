package am.ik.eget2.bootstrap;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import am.ik.eget2.task.movie.download.DownloadMonitor;
import am.ik.eget2.task.movie.download.DownloadWork;

public class CheckCurrentDownloadWork {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:/META-INF/spring/app-context.xml");
        DownloadMonitor downloadMonitor = ctx.getBean(DownloadMonitor.class);
        System.out.println("== working ==");
        List<DownloadWork> working = downloadMonitor.getWorkingList();
        for (DownloadWork work : working) {
            System.out.printf("movie=%s, begin=%s, end=%s, status=%s%n",
                    work.getMovie(), work.getBegin(), work.getEnd(),
                    work.getStatus());
        }
        System.out.println("== finished ==");
        List<DownloadWork> finished = downloadMonitor.getFinishedList();
        for (DownloadWork work : finished) {
            System.out.printf("movie=%s, begin=%s, end=%s, status=%s%n",
                    work.getMovie(), work.getBegin(), work.getEnd(),
                    work.getStatus());
        }
        System.exit(0);
    }
}
