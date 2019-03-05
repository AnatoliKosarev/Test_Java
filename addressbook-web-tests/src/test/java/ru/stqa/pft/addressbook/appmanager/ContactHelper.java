package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactHelper extends HelperBase {
  public ContactHelper(WebDriver driver) {
    super(driver);
  }
  public void fillContactForm(ContactData contactData) {
    type(By.name("firstname"),contactData.getFirstname());
    type(By.name("lastname"),contactData.getLastname());
    type(By.name("home"),contactData.getPhone());
    type(By.name("email"),contactData.getEmail());
  }

  public void submitContactCreation() {
    click(By.name("update"));
  }

  public void initContactModification() {
    click(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='test_ignore@test.com'])[1]/following::img[2]"));
  }

}
