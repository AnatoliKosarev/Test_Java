package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.testng.Assert.fail;

public class TestBase {

  Logger logger = LoggerFactory.getLogger(TestBase.class);

  protected static final ApplicationManager app = new ApplicationManager(System.getProperty("browser", BrowserType.CHROME)); // если браузер указан в editConfigs используется он (-Dbrowser=firefox),
  // если нет - второй дефолтный параметр CHROME

  @BeforeSuite(alwaysRun = true)
  public void setUp() throws Exception {
    app.init();
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() throws Exception {
    app.stop();
    String verificationErrorString = app.verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  @BeforeMethod() //запускается для логирования перед каждым тестовым методом
  public void logTestStart(Method m, Object[] p) { // в кач-ве параметров передаем имя тестового метода + параметры, если тест параметризованный
    logger.info("Start test " + m.getName() + "with Parameters " + Arrays.asList(p)); // выводим сообщение в консоль + сохраняем в файл
  }

  @AfterMethod(alwaysRun = true) //запускается для логирования после каждого тестового метода
  public void logTestStop(Method m, Object[] p) { // в кач-ве параметров передаем имя тестового метода + параметры, если тест параметризованный
    logger.info(("Stop test " + m.getName() + "with Parameters " + Arrays.asList(p))); // выводим сообщение в консоль + сохраняем в файл
  }

}
