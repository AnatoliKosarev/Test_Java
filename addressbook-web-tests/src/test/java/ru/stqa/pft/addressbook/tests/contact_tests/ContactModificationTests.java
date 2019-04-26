package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.Comparator;
import java.util.List;

public class ContactModificationTests extends TestBase {
  String groupName = "test1";

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName(groupName));
    }

    app.goTo().HomePage();
    if (app.contact().list().size() == 0) { //если множество контактов пустое,то
      app.contact().create(new ContactData().withFirstname("test name 1").withFirstname("test last name 1").withPhone("12345").withEmail("test_ignore@test.com").withGroupName(groupName), true);
    }
  }

  @Test
  public void testContactModification () {

    List<ContactData> before = app.contact().list(); //создаем перечень до модификации
    int index = before.size() - 1;
    ContactData contact = new ContactData().withId(before.get(index).getId()).withFirstname("test name 5").withLastname("test last name 6").withPhone("678910").withEmail("test2_ignore@test.com"); //создаем локальную переменную, передаем id последнего контакта из старого списка
    app.contact().modify(index, contact);
    List<ContactData> after = app.contact().list(); //создаем список после модификации

    Assert.assertEquals(after.size(), before.size()); //проверка размеров списков

    before.remove(before.size() - 1); //из старого списка удаляем последний контакт до модификации
    before.add(contact); //в старый список добавляем модифицированный контакт
    Comparator<? super ContactData> byId = Comparator.comparingInt(ContactData::getId); //сравнение по параметру id
    before.sort(byId); //сортировка старого списка по id
    after.sort(byId); //сортировка нового списка по id
    Assert.assertEquals(after, before); //сравнение на идентичность списков
  }
}
