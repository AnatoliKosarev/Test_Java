package ru.stqa.pft.addressbook.generators;

import ru.stqa.pft.addressbook.model.GroupData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class GroupDataGenerator {
  public static void main (String args[]) throws IOException { //пробрасываем exception, поскольку дальше некуда - если исключение - прога ляжет, но будет информативное сообщение

    int count = Integer.parseInt(args [0]); // в кач-ве параметра передаем кол-во групп
    File file = new File(args[1]); // в кач-ве параметра передаем относительный путь до файла

    List<GroupData> groups = generateGroups(count); // генерируем группы
    
    save(groups, file); //создаем и сохраняем файл с группами по указанному адресу
  }

  private static void save(List<GroupData> groups, File file) throws IOException { // пробрасываем exception в main метод
    Writer writer = new FileWriter(file); //открываем файл на запись, в кач-ве параметра передаем путь
    for (GroupData group: groups) { //проходимся в цикле по всем группам переменной group
      writer.write(String.format("%s; %s; %s\n", group.getName(), group.getHeader(), group.getFooter())); // записываем каждую группу в файл,
      // Вместо %s подставляется name/header/footer, затем перенос на новую строку
    }
    writer.close(); // закрываем writer для сохранения данных на диск из кэша
  }

  private static List<GroupData> generateGroups(int count) { //генерируем группы в соответствии с указанным кол-вом
    List<GroupData> groups = new ArrayList<GroupData>(); // создаем пустой список groups
    for (int i = 0; i < count; i++) { // в цикле создаем группы, добавляем в список groups
      groups.add(new GroupData().withName(String.format("test %s", i)).withHeader(String.format("header %s", i)). // подстановка вместо %s порядкового номера группы
              withFooter(String.format("footer %s", i)));
    }
    return groups; // возвращаем список
  }


}
