package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.Comparator;
import java.util.List;

public class GroupModificationTests extends TestBase {

  @Test
  public void testGroupModification() {
    app.getNavigationHelper().gotoGroupPage();

    if (! app.getGroupHelper().isThereAGroup()) {
      app.getGroupHelper().createGroup(new GroupData("test1", null, null));
    }

    List<GroupData> before = app.getGroupHelper().getGroupList();
    app.getGroupHelper().selectGroup(before.size() - 1);
    app.getGroupHelper().initGroupModification();
    GroupData group = new GroupData(before.get(before.size() - 1).getId(), "test1", "test2", "test3"); //передаем id последней группы из списка before (до модификации) и др. параметры
    app.getGroupHelper().fillGroupForms(group);
    app.getGroupHelper().submitGroupModification();
    app.getGroupHelper().returntoGroupPage();
    List<GroupData> after = app.getGroupHelper().getGroupList();

    Assert.assertEquals(after.size(), before.size());

    before.remove(before.size() - 1); // убираем последнюю группу до модифкации
    before.add(group); // добавляем последнюю группу после модификации
    Comparator<? super GroupData> byId = Comparator.comparingInt(GroupData::getId); // сравниваем по идентификатору
    before.sort(byId); // сортировка по идентификатору
    after.sort(byId);
    Assert.assertEquals(after, before); // сравниваем отсортированные по идентификатору списки
  }
}
