package ru.stqa.pft.addressbook.tests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;

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
    List<GroupData> result = session.createQuery( "from GroupData" ).list(); // создаем список типа GroupData в который записываем данные по результатам выборки
    for ( GroupData group : result ) { // пробегаемся по списку переменной group
      System.out.println(group); // выводим прочитанные данные на консоль
    }
    session.getTransaction().commit();
    session.close(); // закрытие сессии
  }
}
