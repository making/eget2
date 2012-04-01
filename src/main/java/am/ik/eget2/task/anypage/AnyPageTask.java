package am.ik.eget2.task.anypage;

import java.net.SocketTimeoutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import am.ik.eget2.queue.TaskQueue;
import am.ik.eget2.task.specificpage.SpecificPage;
import am.ik.eget2.visited.Visited;

public class AnyPageTask implements Runnable, InitializingBean {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AnyPageTask.class);

    private TaskQueue<AnyPage> anyPageQueue;

    private TaskQueue<SpecificPage> specificPageQueue;

    private Visited<String> visitedUrl;

    private String rootUrl;

    private String anyPageUrlPrefix;

    private String specificPageTitlePrefix;

    public void setAnyPageQueue(TaskQueue<AnyPage> anyPageQueue) {
        this.anyPageQueue = anyPageQueue;
    }

    public void setSpecificPageQueue(TaskQueue<SpecificPage> specificPageQueue) {
        this.specificPageQueue = specificPageQueue;
    }

    public void setVisitedUrl(Visited<String> visitedUrl) {
        this.visitedUrl = visitedUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public void setAnyPageUrlPrefix(String anyPageUrlPrefix) {
        this.anyPageUrlPrefix = anyPageUrlPrefix;
    }

    public void setSpecificPageTitlePrefix(String specificPageTitlePrefix) {
        this.specificPageTitlePrefix = specificPageTitlePrefix;
    }

    @Override
    public void run() {
        LOGGER.trace("begin any page task");
        AnyPage anyPage = anyPageQueue.pull();
        LOGGER.trace("pull any page {}", anyPage);
        if (anyPage != null && !visitedUrl.isVisited(anyPage.getUrl())) {
            // 未訪問URLの場合
            String url = anyPage.getUrl();
            try {
                Document root = Jsoup.connect(url).get();
                // 訪問ページから次の訪問先を全て取得
                Elements as = root.select("a[href^=" + anyPageUrlPrefix + "]");
                for (Element a : as) {
                    String href = a.attr("href");
                    String title = a.attr("title");
                    anyPageQueue.push(new AnyPage(href));
                    if (title != null
                            && title.startsWith(specificPageTitlePrefix)) {
                        // 特定のページの場合
                        specificPageQueue.push(new SpecificPage(href));
                    }
                }
                // 訪問済みに
                visitedUrl.visit(url);
            } catch (SocketTimeoutException e) {
                LOGGER.warn("retry page=" + url);
                // キューに追加してリトライ
                anyPageQueue.push(anyPage);
            } catch (Exception e) {
                LOGGER.error("cannot open page=" + url, e);
            }
        }
        LOGGER.trace("end any page task");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (rootUrl != null && rootUrl.length() > 0) {
            // 訪問開始ページを追加
            LOGGER.info("visit from {}", rootUrl);
            anyPageQueue.push(new AnyPage(rootUrl));
        }
    }
}
