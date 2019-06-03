package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.sql.*;

public class DbConnectionTest {

  @Test
  public void testDbConnection() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/addressbook?user=root&password="); // соединяемся с БД

      Statement st = conn.createStatement(); // создаем объект типа Statement
      ResultSet rs = st.executeQuery("select group_id, group_name, group_header, group_footer from group_list"); // вызываем у объекта st метод выборки, делаем выборку из БД
      // результат выборки укладываем в переменной rs (тип похожий на коллекцию, но для него нельзя использовать for)

      Groups groups = new Groups(); // создаем пустой множество

      while (rs.next()) { // пока строка в rs не пустая
        groups.add(new GroupData().withId(rs.getInt("group_id")).withName(rs.getString("group_name")). // читаем и заполняем множество groups данными из выборки
                withHeader(rs.getString("group_header")).withFooter(rs.getString("group_footer")));
      }

      st.close(); // закрываем Statement, что означает, что больше выборок не будем делать, что освобождает занятые ресурсы
      rs.close(); // закрываем rs, что означает,что ничего оттуда больше не будем читать, что освобождает память
      conn.close(); // закрываем соединение с БД

      System.out.println(groups); // выводим на консоль полученный из БД список групп согласно параметрам выборки

       } catch (SQLException ex) {
      // handle any errors
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }
  }
}
