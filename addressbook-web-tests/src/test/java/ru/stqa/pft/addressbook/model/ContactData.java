package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.File;
import java.util.Objects;

@Entity // для привязки к БД для Hibernate
@Table (name = "addressbook") // для привязки к таблице БД для Hibernate

public class ContactData {

  @Id // для привязки к таблице БД для Hibernate - т.к. id используется как идентификатор, поэтому ему присваивается особая аннотация
  @Column (name = "id") // для привязки к столбцу в таблице БД для Hibernate
  private int id = Integer.MAX_VALUE;

  @Expose
  @Column (name = "firstname") // для привязки к столбцу в таблице БД для Hibernate
  private String firstname;

  @Expose
  @Column (name = "lastname") // для привязки к столбцу в таблице БД для Hibernate
  private String lastname;

  @Expose
  @Column (name = "address") // для привязки к столбцу в таблице БД для Hibernate
  @Type(type = "text") // описание типа для преобразования для Hibernate
  private String address;

  @Expose
  @Column (name = "home") // для привязки к столбцу в таблице БД для Hibernate
  @Type(type = "text") // описание типа для преобразования для Hibernate
  private String homePhone;

  @Expose
  @Column (name = "mobile") // для привязки к столбцу в таблице БД для Hibernate
  @Type(type = "text") // описание типа для преобразования для Hibernate
  private String mobilePhone;

  @Expose
  @Column (name = "work") // для привязки к столбцу в таблице БД для Hibernate
  @Type(type = "text") // описание типа для преобразования для Hibernate
  private String workPhone;

  @Transient // для полей которые надо пропустить при выборке из БД для Hibernate
  private String allPhones;

  @Expose
  @Column (name = "email") // для привязки к столбцу в таблице БД для Hibernate
  @Type(type = "text") // описание типа для преобразования для Hibernate
  private String email;

  @Transient // для полей которые надо пропустить при выборке из БД для Hibernate
  private String group;

  @Transient // для полей которые надо пропустить при выборке из БД для Hibernate
  private String contactDetails;

  @Column (name = "photo") // для привязки к столбцу в таблице БД для Hibernate
  @Type(type = "text") // описание типа для преобразования для Hibernate
  private String photo; // т.к. в таблице БД хранится в виде строки

  public ContactData withId(int id) {
    this.id = id;
    return this;
  }

  public ContactData withFirstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  public ContactData withLastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  public ContactData withAddress(String address) {
    this.address = address;
    return this;
  }

  public ContactData withHomePhone(String home) {
    this.homePhone = home;
    return this;
  }

  public ContactData withMobilePhone(String mobile) {
    this.mobilePhone = mobile;
    return this;
  }

  public ContactData withWorkPhone(String work) {
    this.workPhone = work;
    return this;
  }

  public ContactData withAllPhones(String allPhones) {
    this.allPhones = allPhones;
    return this;
  }

  public ContactData withEmail(String email) {
    this.email = email;
    return this;
  }

  public ContactData withGroupName(String group) {
    this.group = group;
    return this;
  }

  public ContactData withContactDetails(String contactInfo) {
    this.contactDetails = contactInfo;
    return this;
  }

  public ContactData withPhoto(File photo) {
    this.photo = photo.getPath(); // т.к. не файл а строка
    return this;
  }

  public int getId() {
    return id;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getAddress() {
    return address;
  }

  public String getHomePhone() {
    return homePhone;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public String getAllPhones() {
    return allPhones;
  }

  public String getEmail() {
    return email;
  }

  public String getGroup() {
    return group;
  }

  public String getContactDetails() {
    return contactDetails;
  }

  public File getPhoto() {
    return new File(photo); // преобразуем в файл
  }

  @Override
  public String toString() {
    return "ContactData{" +
            "id=" + id +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", address='" + address + '\'' +
            ", homePhone='" + homePhone + '\'' +
            ", mobilePhone='" + mobilePhone + '\'' +
            ", workPhone='" + workPhone + '\'' +
            ", allPhones='" + allPhones + '\'' +
            ", email='" + email + '\'' +
            ", group='" + group + '\'' +
            '}';
  }

}
