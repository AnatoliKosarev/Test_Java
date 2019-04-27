package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import ru.stqa.pft.addressbook.tests.TestBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();

    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName("test1"));
    }
  }

  @Test
  public void testGroupModification() {

    Groups before = app.group().all(); //создаем множество типа Groups до модификации группы
    GroupData modifiedGroup = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    GroupData group = new GroupData().
            withId(modifiedGroup.getId()).withName("test1").withHeader("test2").withFooter("test3"); //передаем id группы из объекта modifiedGroup из списка before (до модификации) и др. параметры
    app.group().modify(group);
    assertThat(app.group().count(), equalTo(before.size())); //сравниваем кол-во элементов со старым множеством
    Groups after = app.group().all(); //создаем множество типа Groups после модификации группы

    assertThat(after, equalTo(before.withModified(modifiedGroup, group))); // сравниваем множества по именам и id,
                                                                           // 1. удалив модифицируемую группу из старого списка до модификации
                                                                           // 2. добавив модифицируемую группу с соответствующим id после модификации
  }
}
