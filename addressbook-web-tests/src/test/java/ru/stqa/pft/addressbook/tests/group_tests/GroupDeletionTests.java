package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.List;


public class GroupDeletionTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();

    if (app.group().list().size() == 0) { //если список групп пустой,то
      app.group().create(new GroupData().withName("test1"));
    }
  }

  @Test
  public void testGroupDeletion() throws Exception {

    List<GroupData> before = app.group().list();
    int index = before.size()-1;
    app.group().delete(index);
    List<GroupData> after = app.group().list();

    Assert.assertEquals(after.size(), before.size() - 1);

    before.remove(index); //удаляем группу с соответствующим индексом

    Assert.assertEquals(after, before);
  }

}
