package am.ik.eget2.bootstrap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class CleanUpDir {

    public static void main(String[] args) {
        InputStream strm = CleanUpDir.class.getClassLoader()
                .getResourceAsStream("eget2.properties");
        try {
            Properties p = new Properties();
            p.load(strm);
            String dirName = p.getProperty("download.dir");
            File downloadDir = new File(dirName);
            File[] movies = downloadDir.listFiles();
            for (File movie : movies) {
                if (movie.isDirectory()) {
                    checkDir(movie);
                }
            }
            System.out.println("finish");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(strm);
        }
    }

    private static void checkDir(File movie) {
        File[] files = movie.listFiles();
        if (files.length == 1) {
            File file = files[0];
            if (file.isDirectory()) {
                checkDir(file);
            } else if (file.getName().equals("url.txt")
                    || file.getName().equals(".DS_Store")) {
                deleteDir(movie);
            }
        } else if (files.length == 0) {
            deleteDir(movie);
        }
    }

    private static void deleteDir(File movie) {
        try {
            System.out.println("delete " + movie);
            FileUtils.deleteDirectory(movie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
