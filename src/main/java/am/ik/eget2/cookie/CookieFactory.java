package am.ik.eget2.cookie;

import java.io.IOException;

import org.apache.commons.httpclient.Cookie;

/**
 * クッキーを取得するファクトリー
 */
public interface CookieFactory {
    Cookie generateSessionId() throws IOException;

    Cookie getSessionId() throws IOException;
}
