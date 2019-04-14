package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupHelper extends HelperBase {


  public GroupHelper(WebDriver driver) {
    super(driver);
  }

  public void returntoGroupPage() {
    click(By.linkText("group page"));
  }

  public void submitGroupCreation() {
    click(By.name("submit"));
  }

  public void fillGroupForms(GroupData groupData) {
    type(By.name("group_name"), groupData.getName());
    type(By.name("group_header"), groupData.getHeader());
    type(By.name("group_footer"), groupData.getFooter());
  }

  public void initGroupCreation() {
    click(By.name("new"));
  }

  public void deleteSelectedGroups() {
    click(By.name("delete"));
  }

  public void selectGroupById(int id) {

    driver.findElement(By.cssSelector("input[value = '"+ id +"']")).click(); // ищем элемент с тэгом input, у которого value = передаваемому id
  }

  public void initGroupModification() {
    click(By.name("edit"));
  }

  public void submitGroupModification() {
    click(By.name("update"));
  }

  public boolean isThereAGroup() {
    return isElementPresent(By.name("selected[]"));
  }

  public void create(GroupData group) {
    initGroupCreation();
    fillGroupForms(group);
    submitGroupCreation();
    returntoGroupPage();
  }

  public void modify(GroupData group) {
    selectGroupById(group.getId()); //передаем методу selectGroupById id из modifiedGroup
    initGroupModification();
    fillGroupForms(group);
    submitGroupModification();
    returntoGroupPage();
  }

  public void deleteById(GroupData group) {
    selectGroupById(group.getId()); //передаем методу selectGroupById id из deletedGroup
    deleteSelectedGroups();
    returntoGroupPage();
  }

  public int getGroupCount() {
    return driver.findElements(By.name("selected[]")).size();
  }


  public Set<GroupData> all() { //создаем множества с группами
    Set<GroupData> groups = new HashSet<>();
    List<WebElement> elements = driver.findElements(By.cssSelector("span.group"));
    for (WebElement element : elements) {
      String name = element.getText();
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
      GroupData group = new GroupData().withId(id).withName(name);
      groups.add(group);
    }
    return groups;
  }

}
