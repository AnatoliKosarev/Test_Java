package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.annotations.*;
import ru.stqa.pft.addressbook.tests.TestBase;


public class GroupDeletionTests extends TestBase {

  @Test
  public void testGroupDeletion() throws Exception {
    app.getNavigationHelper().gotoGroupPage();
    app.getGroupHelper().selectGroup();
    app.getGroupHelper().deleteSelectedGroups();
    app.getGroupHelper().returntoGroupPage();
  }

}
