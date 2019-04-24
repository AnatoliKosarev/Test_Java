package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.ArrayList;
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
    type(By.name("home"),contactData.getPhone());
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

  public void initContactModification(int index) {

    driver.findElements(By.cssSelector("img[alt='Edit']")).get(index).click(); //получаем порядковый индекс селектора, кликаем по нему
  }

  public void selectContact(int index) {

    driver.findElements(By.name("selected[]")).get(index).click(); //получаем порядковый индекс селектора, кликаем по нему
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

  public void modify(int index, ContactData contact) {
    initContactModification(index); //выбираем последний контакт
    fillContactForm(contact, false); //модифицируем контакт
    submitContactModification();
    returntoHomePage();
  }

  public void delete(int index) {
    selectContact(index);
    deleteContact();
    acceptAlertDialog();
    returntoHomePage();
  }

  public List<ContactData> list() {
    List<ContactData> contacts = new ArrayList<>(); //создаем основной список
    List<WebElement> elements = driver.findElements(By.name("entry")); //создаем вспомогательный список
    for (WebElement element : elements) { //element пробегает по вспомогательному списку
      String firstname = element.findElement(By.xpath("td[3]")).getText(); //берем текст из 3 колонки соотв. строки
      String lastname = element.findElement(By.xpath("td[2]")).getText(); //берем текст из 2 колонки соотв. строки
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value")); //берем id из соотв. строки
      ContactData contact = new ContactData(id, firstname, lastname, null, null, null); //создаем контакт, передаем ему полученные id, lastname, firstname
      contacts.add(contact); //добавляем контакт в основной список
    }
    return contacts; //возвращаем основной список
  }
}
