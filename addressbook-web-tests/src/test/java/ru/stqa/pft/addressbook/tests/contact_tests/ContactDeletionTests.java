package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

public class ContactDeletionTests extends TestBase {

  @Test
  public void testContactDeletion () {
    app.getNavigationHelper().gotoGroupPage();

    if (! app.getGroupHelper().isThereAGroup()) {
      app.getGroupHelper().createGroup(new GroupData("test1", null, null));
    }

    app.getNavigationHelper().gotoHomePage();

    if (! app.getContactHelper().isThrereAContact()) {
      app.getContactHelper().createContact(new ContactData("test name 1", "test last name 1", "12345", "test_ignore@test.com", "test1"), true);
    }

    app.getContactHelper().selectContact();
    app.getContactHelper().deleteContact();
    app.getContactHelper().acceptAlertDialog();
    app.getContactHelper().returntoHomePage();
  }
}
