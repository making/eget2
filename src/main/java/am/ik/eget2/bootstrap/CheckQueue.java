package am.ik.eget2.bootstrap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import am.ik.eget2.task.movie.Movie;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

public class CheckQueue {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:/META-INF/spring/app-context.xml");
        HazelcastInstance instance = ctx.getBean(HazelcastInstance.class);
        IQueue<Movie> movieQueue = instance.getQueue("movieQueue");
        System.out.println(movieQueue.size());
        for (Movie m : movieQueue) {
            System.out.println(m);
        }
        System.exit(0);
    }

}
