package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName("test1"));
    }

    app.goTo().HomePage();
    if (app.contact().all().size() == 0) { //если множество контактов пустое,то
      app.goTo().groupPage();
      String groupName = app.contact().getGroupName().getGroup();
      app.contact().create(new ContactData().withFirstname("test name 1").withLastname("test last name 1").withAddress("City 1, Str. 2, Bl. 3, App. 4").
              withHomePhone("123").withMobilePhone("456").withWorkPhone("789").withEmail("test_ignore@test.com").withGroupName(groupName), true);
    }
  }

  @Test
  public void testContactDeletion () {

    Contacts before = app.contact().all(); //создаем множество до удаления контакта
    ContactData deletedContact = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    app.contact().delete(deletedContact);
    assertThat(app.contact().count(), equalTo(before.size()-1)); //hash предпроверка - сравниваем кол-во элементов после удаления контакта со старым списком-1
    Contacts after = app.contact().all(); //если кол-во совпало - создаем множество после создания контакта

    assertThat(after, equalTo(before.without(deletedContact))); // сравниваем множества по id и др. параметрам указанным в ContactData, удалив контакт из старого множества
  }
}
