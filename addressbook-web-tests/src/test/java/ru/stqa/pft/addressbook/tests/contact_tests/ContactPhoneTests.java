package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

public class ContactPhoneTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName("test1"));
    }
  }

  @Test (enabled = false)

  public void testContactPhones() {

    app.goTo().HomePage();

    if (! app.contact().isThrereAContact()) {
      app.contact().create(new ContactData("test name 1", "test last name 1", "12345", "test_ignore@test.com", "test1"), true);
    }

    ContactData contact = app.contact().list().iterator().next();
    //ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);
  }
}
