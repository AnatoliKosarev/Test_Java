package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  public void initContactModification() {

    click(By.cssSelector("img[alt='Edit']"));
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
    returntoHomePage();
  }

  public void modify(ContactData contact) {
    selectContactById(contact.getId());//передаем методу selectContactById id из modifiedContact
    initContactModification();
    fillContactForm(contact, false); //модифицируем контакт
    submitContactModification();
    returntoHomePage();
  }

  public void delete(ContactData contact) {
    selectContactById(contact.getId()); //передаем методу selectContactById id из deletedContact
    deleteContact();
    acceptAlertDialog();
    returntoHomePage();
  }

  public Set<ContactData> all() {
    Set<ContactData> contacts = new HashSet<>(); //создаем основное множество
    List<WebElement> elements = driver.findElements(By.name("entry")); //создаем вспомогательный список
    for (WebElement element : elements) { //element пробегает по вспомогательному списку
      String firstname = element.findElement(By.xpath("td[3]")).getText(); //берем текст из 3 колонки соотв. строки
      String lastname = element.findElement(By.xpath("td[2]")).getText(); //берем текст из 2 колонки соотв. строки
      String address = element.findElement(By.xpath("td[4]")).getText(); //берем текст из 4 колонки соотв. строки
      String home = element.findElement(By.xpath("td[6]")).getText(); //берем текст из 6 колонки соотв. строки
      String email = element.findElement(By.xpath("td[5]")).getText(); //берем текст из 5 колонки соотв. строки
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value")); //берем id из соотв. строки

      contacts.add(new ContactData().withId(id).withFirstname(firstname).withLastname(lastname).withAddress(address).withHome(home).withEmail(email)); //создаем контакт, передаем ему полученные id и др. параметры
                                                                                                                                                      //добавляем контакт в основной список
    }
    return contacts; //возвращаем основной список
  }
}
