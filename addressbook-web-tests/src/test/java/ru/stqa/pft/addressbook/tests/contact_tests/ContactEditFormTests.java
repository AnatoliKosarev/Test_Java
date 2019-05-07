package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactEditFormTests extends TestBase {
  String groupName = "test1";

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName(groupName));
    }

    app.goTo().HomePage();
    if (app.contact().all().size() == 0) { //если множество контактов пустое,то
      app.contact().create(new ContactData().withFirstname("test name 1").withLastname("test last name 1").withAddress("City 1, Str. 2, Bl. 3, App. 4").
              withHomePhone("123").withMobilePhone("456").withWorkPhone("789").withEmail("test_ignore@test.com").withGroupName(groupName), true);
    }
  }

  @Test

  public void testContactPhones() {

    app.goTo().HomePage();
    ContactData contact = app.contact().all().iterator().next(); // создаем множество контактов, выбираем первый попавшийся
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact); // загружаем инфо выбранного контакта из формы редактирования

    assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromEditForm))); // сравниваем телефоны с Home page с телефонами с Edit формы, предварительно их склеив с помощью mergePhones
  }

  @Test

  public void testContactAddress() {

    app.goTo().HomePage();
    ContactData contact = app.contact().all().iterator().next(); // создаем множество контактов, выбираем первый попавшийся
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact); // загружаем инфо выбранного контакта из формы редактирования

    assertThat(contact.getAddress(), equalTo(contactInfoFromEditForm.getAddress())); // сравниваем адреса с Home page с адресами с Edit формы
  }

  @Test

  public void testContactEmail() {

    app.goTo().HomePage();
    ContactData contact = app.contact().all().iterator().next(); // создаем множество контактов, выбираем первый попавшийся
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact); // загружаем инфо выбранного контакта из формы редактирования

    assertThat(contact.getEmail(), equalTo(contactInfoFromEditForm.getEmail())); // сравниваем email с Home page с email с Edit формы
  }

  private String mergePhones(ContactData contact) {
    return  Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone()).stream().filter((s) -> !s.equals("")).
            map(ContactEditFormTests::cleaned).collect(Collectors.joining("\n"));
    //1. формируем коллекцию телефонов  2. переводим ее в поток.  3. фильтруем - оставляем только не пустые элементы.  4. применяем ф-цию cleaned с помощью map к отфильтрованным элементам потока
    // 5. склеиваем отфильтрованные элементы с помощью коллектора "joining", в кач-ве параметра ему передаем разделитель \n, это та строка, которая будет вставляться между склеиваемыми элементами
  }

  public static String cleaned (String phone) {
    return phone.replaceAll("\\s", "").replaceAll("[-()]", ""); // заменяем все вхождения 1 пар. на 2 пар.: все пробелы, tab, переводы строки на пустое значение, все дефисы, () на пустышку (т.е. убираем)
    // так как пробелы, дефисы, (), введенные для телефонов на edit форме - не отображаются на Home page, поэтому их надо почистить при сравнении
  }
}
