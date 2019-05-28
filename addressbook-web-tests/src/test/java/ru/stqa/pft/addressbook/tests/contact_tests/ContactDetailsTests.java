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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDetailsTests extends TestBase {

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

  String groupName = "test1";

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName(groupName));
    }
  }

  @Test(dataProvider = "validContactsFromJson")
  public void testContactInfo(ContactData contact) {
    app.goTo().HomePage();
    app.contact().createWithoutGroup(contact); // создаем контакт без выбора группы с помощью параметра из провайдера тестовых данных
    Contacts after = app.contact().all(); // создаем множество контактов после создания
    ContactData createdContact = contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()); // создаем переменную createdContact в которой содержится последний созданный контакт (с max id)
    ContactData contactInfoFromDetailsForm = app.contact().infoFromDetailsForm(createdContact); // загружаем инфо последнего созданного контакта со страницы Details в переменную contactInfoFromDetailsForm
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(createdContact); // загружаем инфо последнего созданного контакта со страницы Edit в переменную contactInfoFromEditForm

    assertThat((mergeDetailsInfo(contactInfoFromDetailsForm)), equalTo(mergeEditInfo(contactInfoFromEditForm))); // сравниваем текст с обеих страниц предварительно приведя его к единому виду
  }

  private String mergeEditInfo(ContactData contact) {
    return Arrays.asList(contact.getFirstname(), contact.getLastname(), contact.getAddress(), contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone(), contact.getEmail()).
            stream().filter((s) -> !s.equals("")).map(ContactDetailsTests::cleanedEditInfo).collect(Collectors.joining("")); // для данных со страницы Edit убираем пустые значения
    // передаем cleaned (убираем все пробелы, табы, переносы строк), параметры отображаем слитно, склеиваем их
  }

  public static String cleanedEditInfo(String phone) {
    return phone.replaceAll("\\s", ""); //убираем все пробелы, табы, переносы строк
  }

  private String mergeDetailsInfo(ContactData contact) {
    return Arrays.asList(contact.getContactDetails()).stream().filter((s) -> !s.equals("")).map(ContactDetailsTests::cleanedDetailsInfo).collect(Collectors.joining(""));
    // для данных со страницы Details убираем пустые значения, передаем cleaned (убираем все пробелы, табы, переносы строк), параметры отображаем слитно, склеиваем их
  }

  public static String cleanedDetailsInfo(String phone) {
    return phone.replaceAll("[H:]", "").replaceAll("[M:]", "").replaceAll("[W:]", "").replaceAll("\\s", ""); // для Details убираем символы, которых нет на Edit,
    //убираем все пробелы, табы, переносы строк
  }
}
