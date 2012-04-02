package am.ik.eget2.task.movie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import am.ik.eget2.cookie.CookieFactory;
import am.ik.eget2.queue.TaskQueue;

public class MovieTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MovieTask.class);

    private TaskQueue<Movie> movieQueue;
    private String downloadDir;
    private CookieFactory cookieFactory;

    public void setMovieQueue(TaskQueue<Movie> movieQueue) {
        this.movieQueue = movieQueue;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }

    public void setCookieFactory(CookieFactory cookieFactory) {
        this.cookieFactory = cookieFactory;
    }

    public synchronized static File createDir(String baseDir, Movie movie)
            throws IOException {
        String title = movie.getTitle();
        if (title.length() >= 100) {
            title = title.substring(0, 100);
        }
        File dir = new File(baseDir, title);
        if (!dir.exists()) {
            LOGGER.trace("create {}", dir);
            FileUtils.forceMkdir(dir);
            File log = new File(dir, "url.txt");
            if (!log.exists()) {
                // メモ
                FileUtils.writeStringToFile(log, movie.getPageUrl(), false);
            }
        }
        return dir;
    }

    @Override
    public void run() {
        LOGGER.trace("begin movie task");
        Movie movie = movieQueue.pull();
        LOGGER.trace("pull movie {}", movie);
        if (movie != null) {
            try {
                download(movie);
            } catch (Exception e) {
                if (movie.getFailedNum().getAndIncrement() < 3) {
                    LOGGER.warn("retry {} because {}", movie, e.getMessage());
                    movieQueue.push(movie);
                } else {
                    LOGGER.error("failed donwload " + movie, e);
                }
            }
        }
        LOGGER.trace("end movie task stop");
    }

    public void download(Movie movie) throws IOException {
        // ログを残す
        File targetDir = createDir(downloadDir, movie);
        String url = movie.getMovieUrl();

        GetMethod method = getResponse(url);
        int statusCode = method.getStatusCode();
        if (statusCode == 200) {
            // HTTPステータスコードが200のときのみ
            String filename = getFilename(method);
            if (filename == null) {
                // throw new RuntimeException("filename is null? @" + movie);
                LOGGER.warn("filename is null @{}", movie);
                return;
            }
            if (checkConcated(targetDir, filename)) {
                LOGGER.debug("already concated {}", url);
                return;
            }
            File log = new File(targetDir, filename + ".log");
            boolean alreadyLogExists = log.exists();
            if (!alreadyLogExists) {
                FileUtils.writeStringToFile(log, new Date().toString());
            }
            // rarの場合
            File rar = new File(targetDir, filename);
            InputStream input = null;
            try {
                if (!rar.exists() || alreadyLogExists) {
                    // rarがまだない、または存在しているがログが残っている(未完了で終了)
                    LOGGER.info("start download {}", movie);
                    input = new BufferedInputStream(
                            method.getResponseBodyAsStream());
                    BufferedOutputStream output = new BufferedOutputStream(
                            new FileOutputStream(rar));
                    IOUtils.copyLarge(input, output);
                    LOGGER.info("end download {}", movie);
                } else {
                    LOGGER.debug("already downloaded {}", url);
                }
                // 成功時にログを削除
                FileUtils.deleteQuietly(log);
            } finally {
                IOUtils.closeQuietly(input);
            }
        } else if (statusCode >= 500 && statusCode < 600) {
            // 50Xの場合はリトライ
            LOGGER.warn("{} error retry {}", statusCode, movie);
            try {
                int count = movie.getFailedNum().get();
                long sleep = (count + 1) * 1000;
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
            }
            if (movie.getFailedNum().getAndIncrement() < 5) {
                download(movie);
            } else {
                LOGGER.error("cannot download {}", movie);
            }
        } else {
            throw new RuntimeException("failed to download " + movie + " ("
                    + statusCode + ")");
        }
    }

    public GetMethod getResponse(String url) throws IOException {
        HttpClient client = new HttpClient();
        Cookie sessionId = cookieFactory.getSessionId();
        client.getState().addCookie(sessionId);
        GetMethod method = new GetMethod(url);
        method.setFollowRedirects(true);
        client.executeMethod(method);
        return method;
    }

    public static String getContentType(GetMethod method) {
        String contentType = null;
        Header h = method.getResponseHeader("Content-Type");
        if (h != null) {
            contentType = h.getValue();
        }
        return contentType;
    }

    public static String getFilename(GetMethod method) {
        String filename = null;
        Header h = method.getResponseHeader("Content-Disposition");
        if (h != null) {
            filename = h.getValue().replace("attachment; filename=", "")
                    .replace("\"", "");
        }
        if (filename != null && filename.length() >= 100) {
            filename = filename.substring(0, 100);
        }
        return filename;
    }

    public static boolean checkConcated(File targetDir, String filename) {
        String base = filename.replaceAll("(\\.part[0-9]+\\.rar)", "");
        return new File(targetDir, base + ".wmv").exists()
                || new File(targetDir, base + ".avi").exists()
                || new File(targetDir, base + ".AVI").exists();
    }
}
