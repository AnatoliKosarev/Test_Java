package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.Comparator;
import java.util.List;

public class GroupModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().groupPage();

    if (app.group().list().size() == 0) { //если список групп пустой,то
      app.group().create(new GroupData().withName("test1"));
    }
  }

  @Test
  public void testGroupModification() {

    List<GroupData> before = app.group().list();
    int index = before.size() - 1; // выбираем порядковый номер группы
    GroupData group = new GroupData().
            withId(before.get(index).getId()).withName("test1").withHeader("test2").withFooter("test3"); //передаем id группы с соответствующим индексом из списка before (до модификации) и др. параметры
    app.group().modify(index, group);
    List<GroupData> after = app.group().list();

    Assert.assertEquals(after.size(), before.size());

    before.remove(index); // убираем группу с соответствующим индексом до модифкации
    before.add(group); // добавляем группу с соответствующим индексом после модификации
    Comparator<? super GroupData> byId = Comparator.comparingInt(GroupData::getId); // сравниваем по идентификатору
    before.sort(byId); // сортировка по идентификатору
    after.sort(byId);
    Assert.assertEquals(after, before); // сравниваем отсортированные по идентификатору списки
  }
}
