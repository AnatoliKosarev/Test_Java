package ru.stqa.pft.addressbook.tests;

import org.hamcrest.MatcherAssert;
import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import ru.stqa.pft.addressbook.tests.contact_tests.ContactEditFormTests;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
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

  @BeforeMethod(alwaysRun = true) //запускается для логирования перед каждым тестовым методом
  public void logTestStart(Method m, Object[] p) { // в кач-ве параметров передаем имя тестового метода + параметры, если тест параметризованный
    logger.info("Start test " + m.getName() + " with Parameters " + Arrays.asList(p)); // выводим сообщение в консоль + сохраняем в файл
  }

  @AfterMethod(alwaysRun = true) //запускается для логирования после каждого тестового метода
  public void logTestStop(Method m, Object[] p) { // в кач-ве параметров передаем имя тестового метода + параметры, если тест параметризованный
    logger.info(("Stop test " + m.getName() + " with Parameters " + Arrays.asList(p))); // выводим сообщение в консоль + сохраняем в файл
  }

  public void verifyGroupListInUI() {
    if (Boolean.getBoolean("verifyUI")) { // если в VM options уазано -DverifyUI=true
      Groups dbGroups = app.db().groups(); // получаем список групп из БД
      Groups uiGroups = app.group().all(); // получаем список групп из UI

      //убираем из данных полученных из БД параметры header и footer, которых не можем получить из UI:
      // все элементы коллекции dbGroups преобразуем в поток, далее применяем упрощающую ф-цию - на входе группа (g), а на выходе новый объект типа GroupData
      //с идентифифкатором и именем как у преобразуемого объекта, а header  и footer не указываем, после того как ко всем элементам применена эта ф-ция
      //собираем полученные эедлементы во множество при помощи коллектора
      assertThat(uiGroups, equalTo(dbGroups.stream().map((g) -> new GroupData().withId(g.getId()).withName(g.getName())).collect(Collectors.toSet())));
    }
  }

    public void verifyContactListInUI() {
      if (Boolean.getBoolean("verifyUI")) { // если в VM options уазано -DverifyUI=true
        Contacts dbContacts = app.db().contacts(); // получаем список контактов из БД
        Contacts uiContacts = app.contact().all(); // получаем список контактов из UI

        //убираем из данных полученных из БД параметры AllPhones для UI, Mobile/Work/Home Phones для БД, которые хз как преобразовать:
        // все элементы коллекции dbContacts / uiContacts преобразуем в поток, далее применяем упрощающую ф-цию - на входе группа (g), а на выходе новый объект типа ContactData
        //с идентифифкатором и именем как у преобразуемого объекта, а телефоны не указываем, после того как ко всем элементам применена эта ф-ция
        //собираем полученные элементы во множество при помощи коллектора
        assertThat(uiContacts.stream().map((g) -> new ContactData().withId(g.getId()).withFirstname(g.getFirstname()).
                        withLastname(g.getLastname()).withAddress(g.getAddress()).withEmail(g.getEmail())).collect(Collectors.toSet()),
                equalTo(dbContacts.stream().map((g) -> new ContactData().withId(g.getId()).withFirstname(g.getFirstname()).
                        withLastname(g.getLastname()).withAddress(g.getAddress()).withEmail(g.getEmail())).collect(Collectors.toSet())));
      }
    }
}
