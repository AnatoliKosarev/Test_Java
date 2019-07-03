package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.WebDriver;

public class RegistrationHelper {

  private final ApplicationManager app;
  private WebDriver driver;

  public RegistrationHelper(ApplicationManager app) {
    this.app = app; // инициализируем ссылку на ApplicationManager
    driver = app.getDriver(); // просим ссылку на driver у ApplicationManager через ленивую инициализацию, т.е. только при вызове метода getDriver
  }

  public void start(String username, String email) {
    driver.get(app.getProperty("web.baseUrl") + "/signup_page.php");
  }
}

