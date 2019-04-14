package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.Set;


public class GroupDeletionTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();

    if (app.group().all().size() == 0) { //если список групп пустой,то
      app.group().create(new GroupData().withName("test1"));
    }
  }

  @Test
  public void testGroupDeletion() throws Exception {

    Set<GroupData> before = app.group().all();
    GroupData deletedGroup = before.iterator().next(); //последовательно перебираем элементы, выбираем первый попавшийся элемент множества
    app.group().deleteById(deletedGroup);
    Set<GroupData> after = app.group().all();

    Assert.assertEquals(after.size(), before.size() - 1);

    before.remove(deletedGroup); //удаляем группу из старого списка

    Assert.assertEquals(after, before); //сравниваем множества по именам и id
  }

}
