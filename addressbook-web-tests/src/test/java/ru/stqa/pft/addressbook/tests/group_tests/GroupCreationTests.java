package ru.stqa.pft.addressbook.tests.group_tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
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

public class GroupCreationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validGroupsFromXml() throws IOException { // создаем провайдер тестовых данных validGroupsFromXml
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.xml")))) { // создаем новый объект reader, передаем имя файла,
      // который хотим прочитать, используем try для автоматического закрытия и сохранения; используем BufferedReader - т.к. в нем есть метод readLine
      String xml = ""; // создаем пустую переменную xml, которую дальше заполняем прочитанными тестовыми данными
      String line = reader.readLine(); // читаем первую строку
      while (line != null) { // читаем строки, пока они не пустые и добавляем их в переменную xml
        xml += line; //т.е. xml = xml + line
        line = reader.readLine(); // читаем следующую строку
      }
      XStream xstream = new XStream(); // создаем новый объект xstream
      xstream.processAnnotations(GroupData.class); // обработка аннотаций в GroupData class
      List<GroupData> groups = (List<GroupData>) xstream.fromXML(xml); // читаем данные типа List<GroupData>  и сохраняем в переменную groups того же типа
      // явно преобразовываем тип (List<GroupData>), т.к. в данном случае знаем, что это список групп
      return groups.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator(); // к каждому объекту применяем ф-цию, которая этот объект
      // заворачивает в массив, состоящий из одного этого объекта; далее после применения ф-ции ко всем объектам, с помощью collect из потока делаем опять
      // List, берем у получившегося списка итератор (переборщик) и возвращаем его
    }
  }

  @DataProvider
  public Iterator<Object[]> validGroupsFromJson() throws IOException { // создаем провайдер тестовых данных validGroupsFromJson
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.json")))) { // создаем новый объект reader, передаем имя файла,
      // который хотим прочитать, используем try для автоматического закрытия и сохранения; используем BufferedReader - т.к. в нем есть метод readLine
      String json = ""; // создаем пустую переменную json, которую дальше заполняем прочитанными тестовыми данными
      String line = reader.readLine(); // читаем первую строку
      while (line != null) { // читаем строки, пока они не пустые и добавляем их в переменную json
        json += line; //т.е. json = json + line
        line = reader.readLine(); // читаем следующую строку
      }
      Gson gson = new Gson(); // создаем новый объект Gson
      List<GroupData> groups = gson.fromJson(json, new TypeToken<List<GroupData>>() {}.getType());
      // читаем данные типа List<GroupData>  и сохраняем в переменную groups того же типа
      // т.к. это сложный объект-список с дженериками (с указанием на то, какого типа данные хранятся в списке) - нельзя просто указать класс, поэтому используем конструкцию с TypeToken
      return groups.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator(); // к каждому объекту применяем ф-цию, которая этот объект
      // заворачивает в массив, состоящий из одного этого объекта; далее после применения ф-ции ко всем объектам, с помощью collect из потока делаем опять
      // List, берем у получившегося списка итератор (переборщик) и возвращаем его
    }
  }

  @Test (dataProvider = "validGroupsFromJson") // привязываем провайдер тестовых данных validGroupsFromJson к тесту
  public void testGroupCreation(GroupData group) throws Exception { // добавляем в тестовый метод параметр GroupData, соотв. передаваемому типу параметров из провайдера тестовых данных

      Groups before = app.db().groups(); //создаем множество типа Groups до создания группы из БД
      app.goTo().groupPage();
      app.group().create(group); // передаем методу create параметр из провайдера тестовых данных
      assertThat(app.group().count(), equalTo(before.size() + 1)); //сравниваем кол-во элементов после добавления группы со старым списком+1
      Groups after = app.db().groups(); //создаем множество типа Groups после создания группы из БД

      assertThat(after, equalTo(before.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt())))); //сравниваем множества по именам и id,
      // 1. добавив в старый список группу с макс. id и указанным именем;
      // 2. находим max id - преобразуем after в поток, с помощью mapToInt преобразуем id элементы потока в числа (т.е. в кач-ве параметра принимает группу (g), а вкач-ве результата выдает id этой группы), находим max, передаем его в group

    verifyGroupListInUI(); // осуществляется проверка отображения списка групп в UI и сравнение с БД если в VM options уазано -DverifyUI=true
  }

  @Test
  public void testBadGroupCreation() throws Exception { // негативный тест - группа с апострофом в имени не должна создаваться, соотв. списки должны быть равны
    app.goTo().groupPage();
    Groups before = app.db().groups();
    GroupData group = new GroupData().withName("test8'"); // не валидный символ '
    app.group().create(group);
    assertThat(app.group().count(), equalTo(before.size()));
    Groups after = app.db().groups();

    assertThat(after, equalTo(before));

    verifyGroupListInUI(); // осуществляется проверка отображения списка групп в UI и сравнение с БД если в VM options уазано -DverifyUI=true

  }

}
