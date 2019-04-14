package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.Set;

public class GroupCreationTests extends TestBase {

  @Test
  public void testGroupCreation() throws Exception {
    app.goTo().groupPage();
    Set<GroupData> before = app.group().all();
    GroupData group = new GroupData().withName("test8");
    app.group().create(group);
    Set<GroupData> after = app.group().all();

    Assert.assertEquals(after.size(), before.size() + 1);

    group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()); //after преобразуем в поток, с помощью mapToInt преобразуем id элементы потока в число, находим max, передаем его в group
    before.add(group);
    Assert.assertEquals(before, after); //сравниваем множества по именам и id
  }

}
