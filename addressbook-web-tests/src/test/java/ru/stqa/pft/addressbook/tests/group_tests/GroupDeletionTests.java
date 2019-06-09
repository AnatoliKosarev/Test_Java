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

    if (app.db().groups().size() == 0) { //если множество групп пустое,то
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("test1"));
    }
  }

  @Test
  public void testGroupDeletion() throws Exception {

    Groups before = app.db().groups(); //создаем множество типа Groups до удаления группы из БД
    GroupData deletedGroup = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    app.goTo().groupPage();
    app.group().deleteById(deletedGroup);
    assertThat(app.group().count(), equalTo(before.size() - 1)); //сравниваем кол-во элементов после удаления группы со старым списком - 1
    Groups after = app.db().groups(); //создаем множество типа Groups после удаления группы из БД

    assertThat(after, equalTo( before.without(deletedGroup))); // сравниваем множества по именам и id, удалив группу из старого списка

    verifyGroupListInUI(); // осуществляется проверка отображения списка групп в UI и сравнение с БД если в VM options уазано -DverifyUI=true
  }

}
