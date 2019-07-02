package ru.stqa.pft.mantis.appmanager;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpSession {
  private CloseableHttpClient httpClient;
  private ApplicationManager app;

  public HttpSession(ApplicationManager app) {
    this.app = app;
    httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build(); // создаем объект HttpClient с учетом возможных перенаправлений
  }

  public boolean login(String username, String password) throws IOException { // в этом методе выполняем логин
    HttpPost post = new HttpPost(app.getProperty("web.baseUrl") + "/login.php"); // создаем пустой post запрос,  указываем адрес, по которому он будет отправлен
    List<NameValuePair> params = new ArrayList<>(); // формируем набор параметров для запроса
    params.add(new BasicNameValuePair("username", username));
    params.add(new BasicNameValuePair("password", password));
    params.add(new BasicNameValuePair("secure_session", "on"));
    params.add(new BasicNameValuePair("return", "index.php"));
    post.setEntity(new UrlEncodedFormEntity(params)); // упаковываем параметры и помещаем в запрос
    CloseableHttpResponse response = httpClient.execute(post); // отпраляем запрос
    String body = getTextFrom(response); // проверяем успешно ли вошел пользователь
    return body.contains(String.format("<span class=\"italic\">%s</span>", username));
  }

  private String getTextFrom(CloseableHttpResponse response) throws IOException {
    try {
      return EntityUtils.toString(response.getEntity());
    } finally {
      response.close();
    }
  }

  public boolean isLoggedInAs(String username) throws IOException { // определяем, какой пользователь сейчас залогинен
    HttpGet get = new HttpGet(app.getProperty("web.baseUrl") + "/index.php"); // заходим на главную страницу
    CloseableHttpResponse response = httpClient.execute(get); // делаем get запрос
    String body = getTextFrom(response);
    return body.contains(String.format("<span class=\"italic\">%s</span>", username)); //проверяем в ответе на get запрос что залогинен интересуемый нас пользователь
  }
}
