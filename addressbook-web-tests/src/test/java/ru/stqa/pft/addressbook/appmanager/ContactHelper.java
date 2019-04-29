package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import java.util.List;

public class ContactHelper extends HelperBase {
  public ContactHelper(WebDriver driver) {
    super(driver);
  }

  public void initContactCreation() {
    click(By.linkText("add new"));
  }

  public void fillContactForm(ContactData contactData, boolean creation) {
    type(By.name("firstname"),contactData.getFirstname());
    type(By.name("lastname"),contactData.getLastname());
    type(By.name("address"),contactData.getAddress());
    type(By.name("home"),contactData.getHome());
    type(By.name("email"),contactData.getEmail());

    if (creation) { //если creation = true проверяем, что на странице есть элемент "new group", выбираем его
      new Select(driver.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
    } else { //если creation = false (not creation) проверяем, что на странице нет элемента "new group"
      Assert.assertFalse(isElementPresent(By.name("new_group")));
    }
  }

  public void submitContactCreation() {
    click(By.name("submit"));
  }

  public void initContactModification(int id) {

    driver.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']", id))).click(); //получаем id modifiedContact, ищем элемент href таким id (вместо %s подставляется id)
    //driver.findElement(By.xpath(String.format("//input[@value ='%s'] /../../              td[8]                  /a", id)))       .click();
                                                 //1.находим чекбокс   2.вверх на 2 ур-ня   3. выбираем 8 ячейку   4.выбираем ссылку  5.кликаем по ссылке
    //driver.findElement(By.xpath(String.format("//tr[.//input[@value ='%s']]              /td[8]                            /a", id)))         .click();
                                                  //1.ищем строку, в которой есть такой id  2.находим в этой строке 8 ячейку 3.выбираем ссылку  4.кликаем по ссылке
  }

  public void selectContactById(int id) {

    driver.findElement(By.cssSelector("input[value = '"+ id +"']")).click(); //ищем элемент с тэгом input, у которого value = передаваемому id
  }

  public void deleteContact() {
    click(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Select all'])[1]/following::input[2]"));
  }

  public void acceptAlertDialog () {
    driver.switchTo().alert().accept();
  }

  public void submitContactModification() {
    click(By.name("update"));
  }

  public void returntoHomePage() {
    click(By.linkText("home"));
  }

  public boolean isThrereAContact() {
    return isElementPresent(By.name("selected[]"));
  }

  public void create(ContactData contact, boolean creation) {
    initContactCreation();
    fillContactForm(contact, true);
    submitContactCreation();
    contactCache = null; //обнуляем кэш, т.к. множество поменялось
    returntoHomePage();
  }

  public void modify(ContactData contact) {
    initContactModification(contact.getId()); //передаем методу initContactModification id из modifiedContact, ищем соотв. элемент, кликаем по соотв. кнопке "Edit"
    fillContactForm(contact, false); //модифицируем контакт
    submitContactModification();
    contactCache = null; //обнуляем кэш, т.к. множество поменялось
    returntoHomePage();
  }

  public void delete(ContactData contact) {
    selectContactById(contact.getId()); //передаем методу selectContactById id из deletedContact
    deleteContact();
    contactCache = null; //обнуляем кэш, т.к. множество поменялось
    acceptAlertDialog();
    returntoHomePage();
  }

  public int count() {
    return driver.findElements(By.name("selected[]")).size();
  }

  private Contacts contactCache = null;

  public Contacts all() { //создаем кэш множества с группами
    if (contactCache != null) { // если кэш не пустой
      return new Contacts(contactCache); // возвращаем копию этого кэш множества
    }
    contactCache = new Contacts(); //если кэш пустой заполняем его - создаем пустое множество типа Contacts
    List<WebElement> elements = driver.findElements(By.name("entry")); //создаем вспомогательный список
    for (WebElement element : elements) { //element пробегает по вспомогательному списку
      String firstname = element.findElement(By.xpath("td[3]")).getText(); //берем текст из 3 колонки соотв. строки
      String lastname = element.findElement(By.xpath("td[2]")).getText(); //берем текст из 2 колонки соотв. строки
      String address = element.findElement(By.xpath("td[4]")).getText(); //берем текст из 4 колонки соотв. строки
      String home = element.findElement(By.xpath("td[6]")).getText(); //берем текст из 6 колонки соотв. строки
      String email = element.findElement(By.xpath("td[5]")).getText(); //берем текст из 5 колонки соотв. строки
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value")); //берем id из соотв. строки

      contactCache.add(new ContactData().withId(id).withFirstname(firstname).withLastname(lastname).withAddress(address).withHome(home).withEmail(email)); //создаем контакт, передаем ему полученные id и др. параметры
                                                                                                                                                      //добавляем контакт в кэш множество
    }
    return new Contacts(contactCache); // возвращаем копию заполненного кэш множества
  }
}
