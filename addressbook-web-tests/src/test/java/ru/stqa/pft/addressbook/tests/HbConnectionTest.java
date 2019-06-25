package ru.stqa.pft.addressbook.tests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.List;

public class HbConnectionTest {

  private SessionFactory sessionFactory; // поле типа SessionFactory для инициализации

  @BeforeClass
  protected void setUp() throws Exception { // процедура инициализации, при кот. чиается конф. файл, проверяются связь с БД, привязки и т.д. (код из доки Hibernate Ch2 Example code)
    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
    try {
      sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
    }
    catch (Exception e) {
      e.printStackTrace(); // если ошибка соединения - выводим текст ошибки на консоль
      // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
      // so destroy it manually.
      StandardServiceRegistryBuilder.destroy( registry );
    }
  }

  @Test
  public void testHbConnection() {
    Session session = sessionFactory.openSession(); // создаем локальную переменную seesion, в которой проводим инициализацию, начинаем сессию
    session.beginTransaction();
    List<ContactData> result = session.createQuery( "from ContactData where deprecated = '0000-00-00'" ).list(); // создаем список типа ContactData в который записываем данные по результатам выборки
    // выборку делаем только для не удаленных контактов - т.к. в БД отображаются удаленные с датой в Deprecated столбце - мы выводим deprecated с нулями, т.е. те которые не удалены
    for ( ContactData contact : result ) { // пробегаемся по списку переменной group
      System.out.println(contact); // выводим прочитанные данные на консоль
      System.out.println(contact.getGroups());
    }
    session.getTransaction().commit();
    session.close(); // закрытие сессии
  }
}
