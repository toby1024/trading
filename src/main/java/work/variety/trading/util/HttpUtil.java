package work.variety.trading.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author zhangbin
 * @date 2018/7/31 10:01
 */
public class HttpUtil {

  public static String get(String url, String jSessionId) throws Exception {
    HttpContext localContext = getHttpContext(jSessionId);
    CloseableHttpClient httpClient = getHttpClient();
    HttpGet get = new HttpGet(url);
    CloseableHttpResponse response = httpClient.execute(get, localContext);
    try {
      String responseBody = EntityUtils.toString(response.getEntity());
      return responseBody;
    } finally {
      response.close();
    }
  }

  private static CloseableHttpClient getHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();

    CloseableHttpClient httpclient = HttpClients.custom()
      .setSSLContext(sslContext)
      .setSSLHostnameVerifier(new NoopHostnameVerifier())
      .build();

    return httpclient;
  }

  private static HttpContext getHttpContext(String jSessionId) {
    BasicCookieStore cookieStore = new BasicCookieStore();
    BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", jSessionId);
    cookie.setDomain("investorservice.cfmmc.com");
    cookie.setPath("/");
    cookieStore.addCookie(cookie);

    HttpContext localContext = new BasicHttpContext();
    localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
    return localContext;
  }
}
