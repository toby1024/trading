package work.variety.trading.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author zhangbin
 * @date 2018/7/26 20:55
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TestCatchData {

  @Test
  public void catchData() throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    BasicCookieStore cookieStore = new BasicCookieStore();
    //JSESSIONID=NHTW3Y5BJEgVo8OoOjjcOtE_1Dnc3VvR_InvmmrRwqkhVEUsAK3n!-1693482384; WMONID=sQj1kIY3rBV

    BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "QTjZTUUEijUsm7MQDVdUUL3fgHk6cKcEdZUdS-DGMpxeKIHjFPD2!-1027631361");
    cookie.setDomain(".investorservice.cfmmc.com");
    cookie.setPath("/");
    cookieStore.addCookie(cookie);

    HttpContext localContext = new BasicHttpContext();
    localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

    SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();

    CloseableHttpClient httpclient = HttpClients.custom()
      .setSSLContext(sslContext)
      .setSSLHostnameVerifier(new NoopHostnameVerifier())
      .build();

    HttpPost httpPost = new HttpPost("https://investorservice.cfmmc.com/customer/setParameter.do");
    httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    CloseableHttpResponse response = httpclient.execute(httpPost, localContext);
    assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
    try {
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        long len = entity.getContentLength();
        if (len != -1 && len < 2048) {
          System.out.println(EntityUtils.toString(entity));
        } else {
          // Stream content out
        }
      }
    } finally {
      response.close();
    }


  }

  @Test
  public void getData() throws Exception{
    String url = "https://investorservice.cfmmc.com/customer/setupViewCustomerDetailFromCompanyAuto.do";
    BasicCookieStore cookieStore = new BasicCookieStore();
    //JSESSIONID=NHTW3Y5BJEgVo8OoOjjcOtE_1Dnc3VvR_InvmmrRwqkhVEUsAK3n!-1693482384; WMONID=sQj1kIY3rBV

    BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "HtruKNkXema-HA0Q4CKIud5_M3aDe36oEJW3In-gOXNyY_hF20SI!2025068771");
    cookie.setDomain("investorservice.cfmmc.com");
    cookie.setPath("/");
    cookieStore.addCookie(cookie);

    HttpContext localContext = new BasicHttpContext();
    localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

    SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();

    CloseableHttpClient httpclient = HttpClients.custom()
      .setSSLContext(sslContext)
      .setSSLHostnameVerifier(new NoopHostnameVerifier())
      .build();

    HttpGet get = new HttpGet(url);
    CloseableHttpResponse response = httpclient.execute(get,localContext);
    assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
    try {
      HttpEntity entity = response.getEntity();
      String responseBody = EntityUtils.toString(response.getEntity());
      System.out.println(responseBody);


    } finally {
      response.close();
    }
  }
}
