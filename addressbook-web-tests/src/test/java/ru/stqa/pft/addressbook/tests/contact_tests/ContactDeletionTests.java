package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;
import java.util.Set;

public class ContactDeletionTests extends TestBase {
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
  public void testContactDeletion () {

    Set<ContactData> before = app.contact().all(); //создаем множество до удаления контакта
    ContactData deletedContact = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    app.contact().delete(deletedContact);
    Set<ContactData> after = app.contact().all();

    Assert.assertEquals(after.size(), before.size()-1); //сравниваем кол-во элементов после удаления группы со старым списком - 1

    before.remove(deletedContact);

    Assert.assertEquals(after, before); // сравниваем множества по id и др. параметрам указанным в ContactData, удалив контакт из старого множества
  }
}
