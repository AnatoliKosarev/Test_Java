package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;

import static org.testng.Assert.fail;

public class TestBase {

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

}
