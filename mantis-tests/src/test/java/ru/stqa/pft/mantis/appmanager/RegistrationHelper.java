package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class RegistrationHelper extends HelperBase {


  public RegistrationHelper(ApplicationManager app) {
    super(app); // вызываем конструктор базового класса (HelperBase), туда передаем ссылку на ApplicationManager
    // убрали ссылки на AppManager, WebDriver, т.к. они есть в и наследуются из HelperBase
  }

  public void start(String username, String email) { // при за полнении используем методы из HelperBase
    driver.get(app.getProperty("web.baseUrl") + "/signup_page.php"); // заходим на страницу регистрации
    type(By.name("username"), username); // заполняем поля соотв. значениями
    type(By.name("email"), email); // заполняем поля соотв. значениями
    click(By.cssSelector("input[value='Signup']")); // нажимаем кнопку Signup
  }

  public void finish(String confirmationLink, String password) {
    driver.get(confirmationLink); // переходим по ссылке в письме
    type(By.name("password"), password); // вводим значение в поле "password"
    type(By.name("password_confirm"), password); // вводим значение в поле "password_confirm"
    click(By.cssSelector("input[value='Update User']")); // нажимаем кнопку Update User
  }
}

