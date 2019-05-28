package ru.stqa.pft.addressbook.tests.group_tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.BeforeMethod;
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

public class GroupModificationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validModifiedGroupsFromJson() throws IOException { // создаем провайдер тестовых данных validModifiedGroupsFromJson
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/modified-groups.json")))) { // создаем новый объект reader, передаем имя файла,
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

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();

    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName("test1"));
    }
  }

  @Test (dataProvider = "validModifiedGroupsFromJson")  // привязываем провайдер тестовых данных validModifiedGroupsFromJson к тесту
  public void testGroupModification(GroupData group) {

    Groups before = app.group().all(); //создаем множество типа Groups до модификации группы
    GroupData modifiedGroup = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    app.group().modify(group.withId(modifiedGroup.getId())); // передаем методу modify параметр из провайдера тестовых данных
    // и id группы из объекта modifiedGroup из списка before (до модификации)
    assertThat(app.group().count(), equalTo(before.size())); //сравниваем кол-во элементов со старым множеством
    Groups after = app.group().all(); //создаем множество типа Groups после модификации группы

    assertThat(after, equalTo(before.withModified(modifiedGroup, group))); // сравниваем множества по именам и id,
                                                                           // 1. удалив модифицируемую группу из старого списка до модификации
                                                                           // 2. добавив модифицируемую группу с соответствующим id после модификации
  }
}
