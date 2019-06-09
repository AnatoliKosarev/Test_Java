package ru.stqa.pft.addressbook.tests.contact_tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validContactsFromJson() throws IOException { // создаем провайдер тестовых данных validContactsFromJson
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/contacts.json")))) {// создаем новый объект reader, передаем имя файла,
      // который хотим прочитать, используем try для автоматического закрытия и сохранения; используем BufferedReader - т.к. в нем есть метод readLine
      String json = ""; // создаем пустую переменную json, которую дальше заполняем прочитанными тестовыми данными
      String line = reader.readLine(); // читаем первую строку
      while (line != null) {  // читаем строки, пока они не пустые и добавляем их в переменную json
        json += line; //т.е. json = json + line
        line = reader.readLine(); // читаем следующую строку
      }
      Gson gson = new Gson(); // создаем новый объект Gson
      List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>() {
      }.getType());
      // читаем данные типа List<ContactData>  и сохраняем в переменную contacts того же типа
      // т.к. это сложный объект-список с дженериками (с указанием на то, какого типа данные хранятся в списке) - нельзя просто указать класс, поэтому используем конструкцию с TypeToken
      return contacts.stream().map((c) -> new Object[]{c}).collect(Collectors.toList()).iterator(); // к каждому объекту применяем ф-цию, которая этот объект
      // заворачивает в массив, состоящий из одного этого объекта; далее после применения ф-ции ко всем объектам, с помощью collect из потока делаем опять
      // List, берем у получившегося списка итератор (переборщик) и возвращаем его
    }
  }

  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().groups().size() == 0) { //если множество групп пустое,то
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("test1"));
    }
  }

  @Test (dataProvider = "validContactsFromJson")
  public void testContactCreation(ContactData contact) {

    app.goTo().groupPage();
    String groupName = app.contact().getGroupName().getGroup();
    app.goTo().HomePage();
    Contacts before = app.db().contacts(); //создаем множество до создания контакта
    File photo = new File("src/test/resources/stru.png"); // инициализируем переменную типа File - указываем относительный путь к файлу с картинкой
    app.contact().create(contact.withGroupName(groupName).withPhoto(photo), true); // передаем методу create параметр из провайдера тестовых данных, имя сущ. группы и фото

    assertThat(app.contact().count(), equalTo(before.size()+1)); //hash предпроверка - сравниваем кол-во элементов после добавления контакта со старым списком+1
    Contacts after = app.db().contacts(); //если кол-во совпало - создаем множество после создания контакта

    assertThat(after, equalTo(before.withAdded(contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt())))); // сравниваем множества по id и др. параметрам указанным в ContactData, добавив контакт в старое множество
                                                                                                                              // 1. добавив в старый список контакт с макс. id и указанными параметрами;
                                                                                                                              // 2. находим max id - преобразуем after в поток, с помощью mapToInt преобразуем id элементы потока в числа (т.е. в кач-ве параметра принимает группу (g), а в кач-ве результата выдает id этой группы), находим max, передаем его в contact

    verifyContactListInUI(); // осуществляется проверка отображения списка контактов в UI и сравнение с БД если в VM options уазано -DverifyUI=true

  }

  @Test ()
  public void testBadContactCreation() { // негативный тест - контакт с апострофом в FirstName не должен создаваться, соотв. списки должны быть равны

    app.goTo().groupPage();
    String groupName = app.contact().getGroupName().getGroup();
    app.goTo().HomePage();
    Contacts before = app.db().contacts(); //создаем множество до создания контакта
    File photo = new File("src/test/resources/stru.png"); // инициализируем переменную типа File - указываем относительный путь к файлу с картинкой
    app.contact().initContactCreation();
    ContactData contact = new ContactData().withFirstname("test name 1'").withGroupName(groupName).withPhoto(photo); // не валидный символ '
    app.contact().create(contact, true);
    assertThat(app.contact().count(), equalTo(before.size())); //hash предпроверка на то что кол-во контактов не поменялось (до создания множества after для ускорения, т.к. count быстрее создания множества, соотв. если кол-во поменялось - тест упадет быстрее)
    Contacts after = app.db().contacts(); //если кол-во не поменялось - тест идет дальше - создаем множество after
    assertThat(after, equalTo(before)); // проверка равенства множеств after и before по указанным в тесте параметрам и id

    verifyContactListInUI(); // осуществляется проверка отображения списка контактов в UI и сравнение с БД если в VM options уазано -DverifyUI=true
  }
}
