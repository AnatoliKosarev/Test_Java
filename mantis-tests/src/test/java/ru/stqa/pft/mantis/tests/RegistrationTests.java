package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class RegistrationTests extends TestBase {

  //@BeforeMethod
  public void startMailServer() { //перед каждым тестом запускаем mail server
    app.mail().start();
  }

  @Test
  public void testRegistration() throws IOException, MessagingException {
    long now = System.currentTimeMillis(); // уникальный идентификатор (текущее время до милисекунды с 1970г.)
    String email = String.format("user%s@localhost.localdomain", now); // email с уникальным идентификатором
    String user = String.format("user1%s", now); // имя пользователя с уникальным идентификатором
    String password = "password";
    app.james().createUser(user, password); // создаем пользователя на удаленном почтовом сервере
    app.registration().start(user, email);
    //List<MailMessage> mailMessages = app.mail().waitForMail(2, 10000);// ждем 2-х писем 10 секунд
    List<MailMessage> mailMessages = app.james().waitForMail(user, password, 60000); //получаем почту из внешнего почтового сервера
    String confirmationLink = findConfirmationLink(mailMessages, email);
    app.registration().finish(confirmationLink, password);
    assertTrue(app.newSession().login(user, password)); // проверяем, что указанный пользователь зарегестрирован
    // и может зайти в систему
  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    MailMessage mailMessage = mailMessages.stream().filter(m -> m.to.equals(email)).findFirst().get();
    // из списка писем ищем то, которое было послано на указанный email
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    // строим регулярное выражение по нахождению ссылки в тексте письма
    // ищем текст "http://", после него должны быть не пробелы, 1 и более
    return regex.getText(mailMessage.text);
    // применяем построенный запрос к письму и возвращаем полученное значение
  }

  //@AfterMethod(alwaysRun = true) //чтобы mail server останавливался даже когда тест упал
  public void stopMailServer() {
    //после каждого теста останавливаем mail server, чтобы письма стирались для избежания конфликтов
    app.mail().stop();
  }
}
