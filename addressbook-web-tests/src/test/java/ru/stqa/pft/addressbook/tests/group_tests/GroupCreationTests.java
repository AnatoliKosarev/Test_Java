package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupCreationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validGroups() throws IOException { // создаем провайдер тестовых данных validGroups
    List<Object[]> list = new ArrayList<Object[]>(); // делаем список массива объектов
    BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.csv"))); // создаем новый объект reader, передаем имя файла, который хотим прочитать
    // BufferedReader - т.к. в нем есть метод readLine
    String line = reader.readLine(); // читаем первую строку
    while (line != null) { // цикл - пока строка в файле не пустая
      String[] split = line.split(";"); // создаем массив, обрабатываем прочитанную строку - разбиваем на куски, кусок определяется по ; в конце, т.е. в кач-ве разделителя используем ";"
      list.add(new Object[] {new GroupData().withName(split[0]).withHeader(split[1]).withFooter(split[2])}); // заполняем список массива объектов прочитанными тест. данными, состоящим из одного объекта GroupData
      line = reader.readLine(); // читаем строку
    }
    return list.iterator(); // возвращаем iterator (переборщик) для списка
  }

  @Test (dataProvider = "validGroups") // привязываем провайдер тестовых данных validGroups к тесту
  public void testGroupCreation(GroupData group) throws Exception { // добавляем в тестовый метод параметр GroupData, соотв. передаваемому типу параметров из провайдера тестовых данных

      app.goTo().groupPage();
      Groups before = app.group().all(); //создаем множество типа Groups до создания группы
      app.group().create(group); // передаем методу create параметр из провайдера тестовых данных
      assertThat(app.group().count(), equalTo(before.size() + 1)); //сравниваем кол-во элементов после добавления группы со старым списком+1
      Groups after = app.group().all(); //создаем множество типа Groups после создания группы

      assertThat(after, equalTo(before.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt())))); //сравниваем множества по именам и id,
      // 1. добавив в старый список группу с макс. id и указанным именем;
      // 2. находим max id - преобразуем after в поток, с помощью mapToInt преобразуем id элементы потока в числа (т.е. в кач-ве параметра принимает группу (g), а вкач-ве результата выдает id этой группы), находим max, передаем его в group
  }

  @Test
  public void testBadGroupCreation() throws Exception { // негативный тест - группа с апострофом в имени не должна создаваться, соотв. списки должны быть равны
    app.goTo().groupPage();
    Groups before = app.group().all();
    GroupData group = new GroupData().withName("test8'"); // не валидный символ '
    app.group().create(group);
    assertThat(app.group().count(), equalTo(before.size()));
    Groups after = app.group().all();

    assertThat(after, equalTo(before));

  }

}
