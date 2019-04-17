package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import ru.stqa.pft.addressbook.tests.TestBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class GroupDeletionTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();

    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName("test1"));
    }
  }

  @Test
  public void testGroupDeletion() throws Exception {

    Groups before = app.group().all(); //создаем множество типа Groups до удаления группы
    GroupData deletedGroup = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    app.group().deleteById(deletedGroup);
    Groups after = app.group().all(); //создаем множество типа Groups после удаления группы

    assertThat(after.size(), equalTo(before.size() - 1));

    assertThat(after, equalTo( before.without(deletedGroup))); // сравниваем множества по именам и id, удалив группу из старого списка
  }

}
