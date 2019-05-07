package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDetailsTests extends TestBase {

  String groupName = "test1";

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName(groupName));
    }
  }

  @Test

  public void testContactInfo() {
    app.goTo().HomePage();
    ContactData contact = new ContactData().withFirstname("test name 1").withLastname("test last name 1").withAddress("City 1, Str. 2, Bl. 3, App. 4"). // создаем переменную контакта с параметрами ддля ввода
            withHomePhone("123").withMobilePhone("456").withWorkPhone("789").withEmail("test_ignore@test.com");

    app.contact().createWithoutGroup(contact); // создаем контакт без выбора группы
    Contacts after = app.contact().all(); // создаем множество контактов после создания
    ContactData createdContact = contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()); // создаем переменную createdContact в которой содержится последний созданный контакт (с max id)
    ContactData contactInfoFromDetailsForm = app.contact().infoFromDetailsForm(createdContact); // загружаем инфо последнего созданного контакта со страницы Details в переменную contactInfoFromDetailsForm
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(createdContact); // загружаем инфо последнего созданного контакта со страницы Edit в переменную contactInfoFromEditForm

    assertThat((mergeDetailsInfo(contactInfoFromDetailsForm)), equalTo(mergeEditInfo(contactInfoFromEditForm))); // сравниваем текст с обеих страниц предварительно приведя его к единому виду
  }

  private String mergeEditInfo(ContactData contact) {
    return Arrays.asList(contact.getFirstname(), contact.getLastname(), contact.getAddress(), contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone(), contact.getEmail()).
            stream().filter((s) -> !s.equals("")).map(ContactDetailsTests::cleanedEditInfo).collect(Collectors.joining("")); // для данных со страницы Edit убираем пустые значения
    // передаем cleaned (убираем все пробелы, табы, переносы строк), параметры отображаем слитно, склеиваем их
  }

  public static String cleanedEditInfo(String phone) {
    return phone.replaceAll("\\s", ""); //убираем все пробелы, табы, переносы строк
  }

  private String mergeDetailsInfo(ContactData contact) {
    return Arrays.asList(contact.getContactDetails()).stream().filter((s) -> !s.equals("")).map(ContactDetailsTests::cleanedDetailsInfo).collect(Collectors.joining(""));
    // для данных со страницы Details убираем пустые значения, передаем cleaned (убираем все пробелы, табы, переносы строк), параметры отображаем слитно, склеиваем их
  }

  public static String cleanedDetailsInfo(String phone) {
    return phone.replaceAll("[H:]", "").replaceAll("[M:]", "").replaceAll("[W:]", "").replaceAll("\\s", ""); // для Details убираем символы, которых нет на Edit,
    //убираем все пробелы, табы, переносы строк
  }
}
