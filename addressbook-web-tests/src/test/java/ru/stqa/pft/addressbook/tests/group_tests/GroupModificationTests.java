package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.Set;

public class GroupModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();

    if (app.group().all().size() == 0) { //если список групп пустой,то
      app.group().create(new GroupData().withName("test1"));
    }
  }

  @Test
  public void testGroupModification() {

    Set<GroupData> before = app.group().all();
    GroupData modifiedGroup = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    GroupData group = new GroupData().
            withId(modifiedGroup.getId()).withName("test1").withHeader("test2").withFooter("test3"); //передаем id группы из объекта modifiedGroup из списка before (до модификации) и др. параметры
    app.group().modify(group);
    Set<GroupData> after = app.group().all();

    Assert.assertEquals(after.size(), before.size());

    before.remove(modifiedGroup); // удаляем группу из старого списка
    before.add(group); // добавляем группу с соответствующим id после модификации
    Assert.assertEquals(after, before); // сравниваем множества по именам и id
  }
}
