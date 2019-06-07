package ru.stqa.pft.addressbook.appmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.List;

public class DbHelper {
  private final SessionFactory sessionFactory;

  public DbHelper() {
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
  }

  public Groups groups() {
    Session session = sessionFactory.openSession(); // создаем локальную переменную seesion, в которой проводим инициализацию, начинаем сессию
    session.beginTransaction();
    List<GroupData> result = session.createQuery( "from GroupData" ).list(); // создаем список типа GroupData в который записываем данные по результатам выборки
    session.close(); // закрытие сессии
    return new Groups(result);
  }

  public Contacts contacts() {
    Session session = sessionFactory.openSession(); // создаем локальную переменную seesion, в которой проводим инициализацию, начинаем сессию
    session.beginTransaction();
    List<ContactData> result = session.createQuery( "from ContactData where deprecated = '0000-00-00'" ).list(); // создаем список типа ContactData в который записываем данные по результатам выборки
    session.close(); // закрытие сессии
    return new Contacts(result);
  }
}