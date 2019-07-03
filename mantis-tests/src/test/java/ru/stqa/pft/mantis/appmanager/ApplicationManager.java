package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {
  private final Properties properties;
  private WebDriver driver; // чтобы никто напрямую не обратился к driver, только через метод getDriver - делаем переменную private
  private String browser;
  private RegistrationHelper registrationHelper;


  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties(); // создаем объект типа Properties
  }


  public void init() throws IOException { // при вызове метода init только загружается конфигурационный файл
    String target = System.getProperty("target", "local");//local используется в кач-ве дефолтного если не указан конф. файл при запуске из консоли
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target)))); // загружаем и читаем конф. файл, вместо %s подставляется указанное target
  }

    public void stop() {
    if (driver != null) { //останавливаем driver только если он был проинициализирован, если не был - нет смысла останавливать
      driver.quit();
    }
  }

    public HttpSession newSession() {
    return new HttpSession(this);
    }

    public String getProperty(String key) {
    return properties.getProperty(key);
    }

  public RegistrationHelper registration() {
    if (registrationHelper == null) { //инициализируем registrationHelper только если он уже не был проинициализирован, т.к. достаточно одного registrationHelper
      registrationHelper = new RegistrationHelper(this); //в качестве параметра передаем ссылку на ApplicationManager
    }
    return registrationHelper;
  }

  public WebDriver getDriver() {
    if (driver == null) { // если driver пустой, т.е. не проинициализирован - инициализируем его и возвращаем;
      if (browser.equals(BrowserType.CHROME)) {
        driver = new ChromeDriver();
      } else if (browser.equals(BrowserType.FIREFOX)) {
        driver = new FirefoxDriver();
      } else if (browser.equals(BrowserType.IE)) {
        driver = new InternetExplorerDriver();
      }
      driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
      driver.get(properties.getProperty("web.baseUrl")); // вместо конкретного адреса используется значение св-ва, которое загружается из внешнего файла
    }
    return driver;
  }
}

