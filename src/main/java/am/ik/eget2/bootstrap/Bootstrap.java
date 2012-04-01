package am.ik.eget2.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * ブートストラップクラス
 *
 */
public class Bootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);
    public static void main(String[] args) {
        LOGGER.info("bootstrapping...");
        ConfigurableApplicationContext ctx = new GenericXmlApplicationContext(
                "classpath:/META-INF/spring/app-context.xml");
        try {
            LOGGER.info("start service");
            Eget2Service service = ctx.getBean(Eget2Service.class);
            service.run();
            LOGGER.info("stop service");
        } finally {
            ctx.close();
        }
        LOGGER.info("exit");
    }
}
