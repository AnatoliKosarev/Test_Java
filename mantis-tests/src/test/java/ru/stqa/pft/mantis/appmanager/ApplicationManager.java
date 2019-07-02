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
  WebDriver driver;
  private String browser;


  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties(); // создаем объект типа Properties
  }


  public void init() throws IOException {
    String target = System.getProperty("target", "local");//local используется в кач-ве дефолтного если не указан конф. файл при запуске из консоли
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target)))); // загружаем и читаем конф. файл, вместо %s подставляется указанное target

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

    public void stop() {
      driver.quit();
    }

    public HttpSession newSession() {
    return new HttpSession(this);
    }

    public String getProperty(String key) {
    return properties.getProperty(key);
    }
  }

