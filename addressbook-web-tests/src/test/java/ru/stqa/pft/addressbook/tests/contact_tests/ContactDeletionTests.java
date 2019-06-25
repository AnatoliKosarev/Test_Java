package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().groups().size() == 0) { //если множество групп пустое,то
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("test1"));
    }

    if (app.db().contacts().size() == 0) { //если множество контактов пустое,то
      Groups groups = app.db().groups();
      File photo = new File("src/test/resources/stru.png"); // инициализируем переменную типа File - указываем относительный путь к файлу с картинкой
      app.goTo().HomePage();
      app.contact().create(new ContactData().withFirstname("test name 1").withLastname("test last name 1").withAddress("City 1, Str. 2, Bl. 3, App. 4").
              withHomePhone("123").withMobilePhone("456").withWorkPhone("789").withEmail("test_ignore@test.com").withPhoto(photo).inGroup(groups.iterator().next()), true);
    }
  }

  @Test
  public void testContactDeletion () {

    Contacts before = app.db().contacts(); //создаем множество до удаления контакта
    ContactData deletedContact = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    app.contact().delete(deletedContact);
    assertThat(app.contact().count(), equalTo(before.size()-1)); //hash предпроверка - сравниваем кол-во элементов после удаления контакта со старым списком-1
    Contacts after = app.db().contacts(); //если кол-во совпало - создаем множество после создания контакта

    assertThat(after, equalTo(before.without(deletedContact))); // сравниваем множества по id и др. параметрам указанным в ContactData, удалив контакт из старого множества

    verifyContactListInUI(); // осуществляется проверка отображения списка контактов в UI и сравнение с БД если в VM options уазано -DverifyUI=true
  }
}
