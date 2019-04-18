package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.List;

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
    groupCache = null; //обнуляем кэш, т.к. список поменялся
    returntoGroupPage();
  }

  public void modify(GroupData group) {
    selectGroupById(group.getId()); //передаем методу selectGroupById id из modifiedGroup
    initGroupModification();
    fillGroupForms(group);
    submitGroupModification();
    groupCache = null; //обнуляем кэш, т.к. список поменялся
    returntoGroupPage();
  }

  public void deleteById(GroupData group) {
    selectGroupById(group.getId()); //передаем методу selectGroupById id из deletedGroup
    deleteSelectedGroups();
    groupCache = null; //обнуляем кэш, т.к. список поменялся
    returntoGroupPage();
  }

  public int getGroupCount() {
    return driver.findElements(By.name("selected[]")).size();
  }

  private Groups groupCache = null;

  public Groups all() { //создаем кэш множества с группами
    if (groupCache != null) { // если кэш не пустой
      return new Groups(groupCache); // возвращаем копию этого кэш множества
    }
    groupCache = new Groups(); //если кэш пустой заполняем его - создаем пустое множество типа Groups
    List<WebElement> elements = driver.findElements(By.cssSelector("span.group"));
    for (WebElement element : elements) { // переменная element пробегается по списку WebElement
      String name = element.getText();
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
      groupCache.add(new GroupData().withId(id).withName(name)); // добавляем в кэш множество группы
    }
    return new Groups(groupCache); // возвращаем копию заполненного кэш множества
  }

}
