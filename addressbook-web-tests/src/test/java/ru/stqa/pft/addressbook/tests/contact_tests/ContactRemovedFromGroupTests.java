package ru.stqa.pft.addressbook.tests.contact_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.io.File;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactRemovedFromGroupTests extends TestBase {
  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("test1"));
    }

    if (app.db().contacts().size() == 0) {
      Groups groups = app.db().groups();
      File photo = new File("src/test/resources/stru.png"); // инициализируем переменную типа File - указываем относительный путь к файлу с картинкой
      app.goTo().HomePage();
      app.contact().create(new ContactData().withFirstname("test name 1").withLastname("test last name 1").withAddress("City 1, Str. 2, Bl. 3, App. 4").
              withHomePhone("123").withMobilePhone("456").withWorkPhone("789").withEmail("test_ignore@test.com").withPhoto(photo).inGroup(groups.iterator().next()), true);
    }
  }

  @Test (enabled = false)
  public void testContactRemovedFromGroup() {
    Contacts before = app.db().contacts();

    ContactData contactRemoved = before.iterator().next();
    int groupId = contactRemoved.getGroups().stream().mapToInt((g) -> g.getId()).max().getAsInt();
    GroupData group = app.db().groups().iterator().next().withId(groupId);

    app.goTo().HomePage();
    app.contact().selectContactsGroup(group.getId());
    app.contact().selectContactById(contactRemoved.getId());
    app.contact().removeContactFromGroup();

    Contacts after = app.db().contacts();

    assertThat(after, equalTo(before));
  }
}
