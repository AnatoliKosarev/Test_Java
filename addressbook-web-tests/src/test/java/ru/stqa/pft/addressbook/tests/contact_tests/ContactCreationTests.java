package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;
import java.util.Set;

public class ContactCreationTests extends TestBase {
  String groupName = "test1";

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName(groupName));
    }
  }

  @Test
  public void testContactCreation() {

    app.goTo().HomePage();
    Set<ContactData> before = app.contact().all(); //создаем множество до создания контакта
    ContactData contact = new ContactData().withFirstname("test name 1").withLastname("test last name 1").
            withAddress("City 1, Str. 2, Bl. 3, App. 4").withHome("12345").withEmail("test_ignore@test.com").withGroupName(groupName); //создаем локальную переменную, передаем id последнего контакта из старого списка и данные ввода
    app.contact().create(contact, true);
    Set<ContactData> after = app.contact().all(); //создаем множество после создания контакта

    Assert.assertEquals(after.size(), before.size()+1); //сравниваем кол-во элементов после добавления группы со старым списком+1

    contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt());  //находим max id - преобразуем after в поток, с помощью mapToInt преобразуем id элементы потока в числа (т.е. в кач-ве параметра принимает группу (g), а в кач-ве результата выдает id этой группы), находим max, передаем его в contact
    before.add(contact);
    Assert.assertEquals(after, before); // сравниваем множества по id и др. параметрам указанным в ContactData, добавив контакт в старое множество

  }

}
