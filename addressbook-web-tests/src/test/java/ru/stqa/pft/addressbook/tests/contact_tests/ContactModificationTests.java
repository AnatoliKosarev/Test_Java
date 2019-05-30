package ru.stqa.pft.addressbook.tests.contact_tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
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

public class ContactModificationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validModifiedContactsFromJson () throws IOException { // создаем провайдер тестовых данных validModifiedContactsFromJson
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/modified-contacts.json")))) { // создаем новый объект reader, передаем имя файла,
      // который хотим прочитать, используем try для автоматического закрытия и сохранения; используем BufferedReader - т.к. в нем есть метод readLine
    String json = ""; // создаем пустую переменную json, которую дальше заполняем прочитанными тестовыми данными
    String line = reader.readLine(); // читаем первую строку
    while (line != null) { // читаем строки, пока они не пустые и добавляем их в переменную json
      json += line;  //т.е. json = json + line
      line = reader.readLine(); // читаем следующую строку
    }
    Gson gson = new Gson(); // создаем новый объект Gson
    List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>() {} .getType());
      // читаем данные типа List<ContactData>  и сохраняем в переменную groups того же типа
      // т.к. это сложный объект-список с дженериками (с указанием на то, какого типа данные хранятся в списке) - нельзя просто указать класс, поэтому используем конструкцию с TypeToken
    return contacts.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator(); // к каждому объекту применяем ф-цию, которая этот объект
      // заворачивает в массив, состоящий из одного этого объекта; далее после применения ф-ции ко всем объектам, с помощью collect из потока делаем опять
      // List, берем у получившегося списка итератор (переборщик) и возвращаем его
    }
  }

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

  @Test (dataProvider = "validModifiedContactsFromJson")
  public void testContactModification(ContactData contact) {

    Contacts before = app.contact().all(); //создаем перечень до модификации
    ContactData modifiedContact = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    app.contact().modify(contact.withId(modifiedContact.getId())); // передаем методу modify параметр из провайдера тестовых данных
    // и id группы из объекта modifiedContact из списка before (до модификации)
    assertThat(app.contact().count(), equalTo(before.size())); //hash предпроверка - сравниваем кол-во элементов после модификации контакта со старым списком
    Contacts after = app.contact().all(); //если кол-во совпало - создаем множество после создания контакта

    assertThat(after, equalTo(before.withModified(modifiedContact, contact))); //сравниваем множества по id и др. параметрам указанным в ContactData
    // 1. из старого множества удаляем последний контакт до модификации
    // 2. в старое множество добавляем модифицированный контакт
  }
}
