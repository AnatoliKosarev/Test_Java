package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.List;

public class ContactDeletionTests extends TestBase {

  @Test
  public void testContactDeletion () {
    app.goTo().groupPage();

    if (! app.group().isThereAGroup()) {
      app.group().create(new GroupData("test1", null, null));
    }

    app.goTo().gotoHomePage();

    if (! app.getContactHelper().isThrereAContact()) {
      app.getContactHelper().createContact(new ContactData("test name 1", "test last name 1", "12345", "test_ignore@test.com", "test1"), true);
    }

    List<ContactData> before = app.getContactHelper().getContactList();
    app.getContactHelper().selectContact(before.size()-1);
    app.getContactHelper().deleteContact();
    app.getContactHelper().acceptAlertDialog();
    app.getContactHelper().returntoHomePage();
    List<ContactData> after = app.getContactHelper().getContactList();

    Assert.assertEquals(after.size(), before.size()-1);

    before.remove(before.size()-1);

    Assert.assertEquals(after, before);
  }
}
