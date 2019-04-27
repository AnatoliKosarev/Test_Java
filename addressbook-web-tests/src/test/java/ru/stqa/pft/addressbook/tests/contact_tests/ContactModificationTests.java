package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;
import java.util.Set;

public class ContactModificationTests extends TestBase {
  String groupName = "test1";

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName(groupName));
    }

    app.goTo().HomePage();
    if (app.contact().all().size() == 0) { //если множество контактов пустое,то
      app.contact().create(new ContactData().withFirstname("test name 1").withLastname("test last name 1").withAddress("City 1, Str. 2, Bl. 3, App. 4").withHome("12345").withEmail("test_ignore@test.com").withGroupName(groupName), true);
    }
  }

  @Test
  public void testContactModification () {

    Set<ContactData> before = app.contact().all(); //создаем перечень до модификации
    ContactData modifiedContact = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    ContactData contact = new ContactData().withId(modifiedContact.getId()).withFirstname("test name 5").withLastname("test last name 6").
            withAddress("City 9, Str. 8, Bl. 7, App. 6").withHome("678910").withEmail("test2_ignore@test.com"); //создаем локальную переменную, передаем id последнего контакта из старого списка и данные ввода
    app.contact().modify(contact);
    Set<ContactData> after = app.contact().all(); //создаем множество после модификации

    Assert.assertEquals(after.size(), before.size()); //сравниваем кол-во элементов со старым множеством

    before.remove(modifiedContact); //из старого множества удаляем последний контакт до модификации
    before.add(contact); //в старое множество добавляем модифицированный контакт
    Assert.assertEquals(after, before); // сравниваем множества по id и др. параметрам указанным в ContactData, добавив bизмененный контакт в старое множество
  }
}
