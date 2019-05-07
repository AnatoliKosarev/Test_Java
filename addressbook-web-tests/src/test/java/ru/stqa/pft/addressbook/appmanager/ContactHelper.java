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
    type(By.name("home"),contactData.getHomePhone());
    type(By.name("mobile"),contactData.getMobilePhone());
    type(By.name("work"),contactData.getWorkPhone());
    type(By.name("email"),contactData.getEmail());

    if (creation) { //если creation = true проверяем, что на странице есть элемент "new group", выбираем его
      new Select(driver.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
    } else { //если creation = false (not creation) проверяем, что на странице нет элемента "new group"
      Assert.assertFalse(isElementPresent(By.name("new_group")));
    }
  }

  public void fillContactFormWithoutGroup(ContactData contactData) { // спец. метод для заполнения контакта без выбора группы для сравнения с Details
    type(By.name("firstname"),contactData.getFirstname());
    type(By.name("lastname"),contactData.getLastname());
    type(By.name("address"),contactData.getAddress());
    type(By.name("home"),contactData.getHomePhone());
    type(By.name("mobile"),contactData.getMobilePhone());
    type(By.name("work"),contactData.getWorkPhone());
    type(By.name("email"),contactData.getEmail());
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


  private void initContactDetails(int id) {
    driver.findElement(By.cssSelector(String.format("a[href='view.php?id=%s']", id))).click(); //получаем id contact, ищем элемент href таким id (вместо %s подставляется id)
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

  public void createWithoutGroup(ContactData contact) { // спец. метод создания контакта без выбора группы для сравнения с Details
    initContactCreation();
    fillContactFormWithoutGroup(contact);
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
    List<WebElement> rows = driver.findElements(By.name("entry")); //создаем вспомогательный список
    for (WebElement row : rows) { //row пробегает по вспомогательному списку
      //так
      List <WebElement> cells = row.findElements(By.tagName("td"));
      int id = Integer.parseInt(cells.get(0).findElement(By.tagName("input")).getAttribute("value")); //берем id из соотв. строки, нумерация ячеек с 0, поэтому 1 ячейка = 0
      String firstname = cells.get(2).getText(); //берем текст из соотв. колонки соотв. строки
      String lastname = cells.get(1).getText(); //берем текст из соотв. колонки соотв. строки
      String address = cells.get(3).getText(); //берем текст из соотв. колонки соотв. строки
      String email = cells.get(4).getText(); //берем текст из соотв. колонки соотв. строки

      //или так
      /*int id = Integer.parseInt(row.findElement(By.tagName("input")).getAttribute("value")); //берем id из соотв. строки
      String firstname = row.findElement(By.xpath("td[2]")).getText(); //берем текст из 3 колонки соотв. строки
      String lastname = row.findElement(By.xpath("td[1]")).getText(); //берем текст из 2 колонки соотв. строки
      String ...*/

      String allPhones = cells.get(5).getText(); //берем телефоны из соотв. колонки соотв. строки одной строкой

      contactCache.add(new ContactData().withId(id).withFirstname(firstname).withLastname(lastname).withAddress(address).
              withAllPhones(allPhones).withEmail(email)); //создаем контакт, передаем ему полученные id и др. параметры, добавляем контакт в кэш множество
    }
    return new Contacts(contactCache); // возвращаем копию заполненного кэш множества
  }

  public ContactData infoFromEditForm(ContactData contact) {
    initContactModification(contact.getId()); //передаем методу initContactModification id из выбранного контакта, ищем соотв. элемент, кликаем по соотв. кнопке "Edit"
    String firstName = driver.findElement(By.name("firstname")).getAttribute("value"); //берем значение из соответствующего поля
    String lastName = driver.findElement(By.name("lastname")).getAttribute("value"); //берем значение из соответствующего поля
    String address = driver.findElement(By.name("address")).getText(); //берем текст из соответствующего поля
    String home = driver.findElement(By.name("home")).getAttribute("value"); //берем значение из соответствующего поля
    String mobile = driver.findElement(By.name("mobile")).getAttribute("value"); //берем значение из соответствующего поля
    String work = driver.findElement(By.name("work")).getAttribute("value"); //берем значение из соответствующего поля
    String email = driver.findElement(By.name("email")).getAttribute("value"); //берем значение из соответствующего поля
    driver.navigate().back(); // возвращаемся на Home page
    return new ContactData().withId(contact.getId()).withFirstname(firstName).withLastname(lastName).withAddress(address).withEmail(email).
            withHomePhone(home).withMobilePhone(mobile).withWorkPhone(work); //возвращаем объект, заполняем соотв. поля полученными значениями
  }

  public ContactData infoFromDetailsForm(ContactData contact) {
    initContactDetails(contact.getId()); //передаем методу initContactDetails id из выбранного контакта, ищем соотв. элемент, кликаем по соотв. кнопке "Details"
    String contactInfo = driver.findElement(By.id("content")).getText(); //берем текст со страницы Details
    driver.navigate().back(); // возвращаемся на Home page
    return new ContactData().withContactDetails(contactInfo); //возвращаем полученный текст
  }
}
