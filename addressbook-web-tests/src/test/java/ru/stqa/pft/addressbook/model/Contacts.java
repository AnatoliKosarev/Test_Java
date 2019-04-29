package ru.stqa.pft.addressbook.model;

import com.google.common.collect.ForwardingSet;
import java.util.HashSet;
import java.util.Set;


public class Contacts extends ForwardingSet <ContactData> {

  private Set<ContactData> delegate; //объект delegate

  public Contacts(Contacts contacts) { // конструктор
    this.delegate = new HashSet<ContactData>(contacts.delegate);  // берем множество из существующего объекта, строим новое и присваиваем его в новый создаваемый этим конструктором объект (т.е. делаем копию этого множества)
  }

  public Contacts () {
    this.delegate = new HashSet<ContactData>();  // конструктор без параметров для метода all из ContactHelper
  }

  @Override
  protected Set<ContactData> delegate() {
    return delegate;
  }

  public Contacts withAdded (ContactData contact) {
    Contacts contacts = new Contacts(this); // создаем новое множество - копию delegate, с помощью конструктора
    contacts.add(contact); //добавляем в множество контакт (метод add работает для множества, т.к. реализован в ForwardingSet)
    return contacts; // возвращаем множество
  }

  public Contacts without (ContactData contact) {
    Contacts contacts = new Contacts(this); // создаем новое множество - копию delegate, с помощью конструктора
    contacts.remove(contact); //убираем из множества контакт (метод remove работает для множества, т.к. реализован в ForwardingSet)
    return contacts; // возвращаем множество
  }

  public Contacts withModified (ContactData modifiedContact, ContactData contact) {
    Contacts contacts = new Contacts(this);
    contacts.remove(modifiedContact);
    contacts.add(contact);
    return contacts;
  }
}
