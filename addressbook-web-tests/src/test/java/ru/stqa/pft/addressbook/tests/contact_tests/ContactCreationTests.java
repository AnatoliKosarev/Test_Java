package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {
  String groupName = "test1";

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName(groupName));
    }
  }

  @Test ()
  public void testContactCreation() {

    app.goTo().HomePage();
    Contacts before = app.contact().all(); //создаем множество до создания контакта
    ContactData contact = new ContactData().withFirstname("test name 1").withLastname("test last name 1").
            withAddress("City 1, Str. 2, Bl. 3, App. 4").withHome("12345").withEmail("test_ignore@test.com").withGroupName(groupName); //создаем локальную переменную, передаем id последнего контакта из старого списка и данные ввода
    app.contact().create(contact, true);
    assertThat(app.contact().count(), equalTo(before.size()+1)); //hash предпроверка - сравниваем кол-во элементов после добавления контакта со старым списком+1
    Contacts after = app.contact().all(); //если кол-во совпало - создаем множество после создания контакта

    assertThat(after, equalTo(before.withAdded(contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt())))); // сравниваем множества по id и др. параметрам указанным в ContactData, добавив контакт в старое множество
                                                                                                                              // 1. добавив в старый список контакт с макс. id и указанными параметрами;
                                                                                                                              // 2. находим max id - преобразуем after в поток, с помощью mapToInt преобразуем id элементы потока в числа (т.е. в кач-ве параметра принимает группу (g), а в кач-ве результата выдает id этой группы), находим max, передаем его в contact

  }

  @Test
  public void testBadContactCreation() { // негативный тест - контакт с апострофом в FirstName не должен создаваться, соотв. списки должны быть равны

    app.goTo().HomePage();
    Contacts before = app.contact().all(); //создаем множество до создания контакта
    ContactData contact = new ContactData().withFirstname("test name 1'").withGroupName(groupName); // не валидный символ '
    app.contact().create(contact, true);
    assertThat(app.contact().count(), equalTo(before.size())); //hash предпроверка на то что кол-во контактов не поменялось (до создания множества after для ускорения, т.к. count быстрее создания множества, соотв. если кол-во поменялось - тест упадет быстрее)
    Contacts after = app.contact().all(); //если кол-во не поменялось - тест идет дальше - создаем множество after
    assertThat(after, equalTo(before)); // проверка равенства множеств after и before по указанным в тесте параметрам и id
  }

}
