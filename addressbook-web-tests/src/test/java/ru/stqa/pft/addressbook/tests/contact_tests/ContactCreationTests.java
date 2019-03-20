package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() {
    app.getNavigationHelper().gotoGroupPage();

    if (! app.getGroupHelper().isThereAGroup()) {
      app.getGroupHelper().createGroup(new GroupData("test1", null, null));
    }

    app.getNavigationHelper().gotoHomePage();
    app.getContactHelper().createContact(new ContactData("test name 1", "test last name 1", "12345", "test_ignore@test.com", "test1"), true);
  }

}
