package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

public class ContactModificationTests extends TestBase {

  @Test
  public void testContactModification () {
    app.getNavigationHelper().gotoGroupPage();

    if (! app.getGroupHelper().isThereAGroup()) {
      app.getGroupHelper().createGroup(new GroupData("test1", null, null));
    }

    app.getNavigationHelper().gotoHomePage();

    if (! app.getContactHelper().isThrereAContact()) {
      app.getContactHelper().createContact(new ContactData("test name 1", "test last name 1", "12345", "test_ignore@test.com", "test1"), true);
    }

    app.getContactHelper().initContactModification();
    app.getContactHelper().fillContactForm(new ContactData("test name 5", "test last name 6", "678910", "test2_ignore@test.com", null), false);
    app.getContactHelper().submitContactModification();
    app.getContactHelper().returntoHomePage();
  }
}
