package ru.stqa.pft.addressbook.tests.group_tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import ru.stqa.pft.addressbook.tests.TestBase;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupCreationTests extends TestBase {

  @Test
  public void testGroupCreation() throws Exception {
    app.goTo().groupPage();
    Groups before = app.group().all(); //создаем множество типа Groups до создания группы
    GroupData group = new GroupData().withName("test8");
    app.group().create(group);
    Groups after = app.group().all(); //создаем множество типа Groups после создания группы

    assertThat(after.size(), equalTo(before.size()+1));

    assertThat(after, equalTo(before.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt())))); //сравниваем множества по именам и id,
                                                                                                                            // 1. добавив в старый список группу с макс. id и указанным именем;
                                                                                                                            // 2. находим max id - преобразуем after в поток, с помощью mapToInt преобразуем id элементы потока в числа (т.е. в кач-ве параметра принимает группу (g), а вкач-ве результата выдает id этой группы), находим max, передаем его в group
  }

}
