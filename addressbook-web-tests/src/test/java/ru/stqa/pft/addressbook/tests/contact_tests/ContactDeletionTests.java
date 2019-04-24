package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.List;

public class ContactDeletionTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();
    if (app.group().all().size() == 0) { //если множество групп пустое,то
      app.group().create(new GroupData().withName("test1"));
    }

    app.goTo().HomePage();
    if (app.contact().list().size() == 0) { //если множество контактов пустое,то
      app.contact().create(new ContactData("test name 1", "test last name 1", "12345", "test_ignore@test.com", "test1"), true);
    }
  }

  @Test
  public void testContactDeletion () {

    List<ContactData> before = app.contact().list();
    int index = before.size()-1;
    app.contact().delete(index);
    List<ContactData> after = app.contact().list();

    Assert.assertEquals(after.size(), before.size()-1);

    before.remove(before.size()-1);

    Assert.assertEquals(after, before);
  }
}
