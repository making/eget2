package am.ik.eget2.cookie.bitshare;

import java.io.IOException;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import am.ik.eget2.cookie.CookieFactory;

/**
 * BitShareからクッキーを取得するファクトリー
 */
public class BitShareCookieFactory implements CookieFactory {
    private final static Logger LOGGER = LoggerFactory
            .getLogger(BitShareCookieFactory.class);
    private Cookie sessionId = null;
    private String user;
    private String password;

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public synchronized Cookie generateSessionId() throws IOException {
        String loginUrl = "http://bitshare.com/login.html";
        HttpClient client = new HttpClient();
        // POSTのパラメータ作成
        PostMethod post = new PostMethod(loginUrl);
        post.addParameter(new NameValuePair("user", user));
        post.addParameter(new NameValuePair("password", password));
        post.addParameter(new NameValuePair("submit", "submit"));
        // POST送信
        client.executeMethod(post);
        for (Cookie c : client.getState().getCookies()) {
            if ("PHPSESSID".equals(c.getName())) {
                sessionId = c;
                LOGGER.debug("generate cookie={}", sessionId);
                return c;
            }
        }
        return null;
    }

    @Override
    public synchronized Cookie getSessionId() throws IOException {
        if (sessionId == null || sessionId.isExpired()) {
            sessionId = generateSessionId();
        }
        return sessionId;
    }

}
