package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import ru.stqa.pft.addressbook.model.GroupData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ModifiedGroupDataGenerator {

  @Parameter(names = "-c", description = "Group count") // указываем какие опции передаем в командной строке, даем опциям имена и описание
  public int count;

  @Parameter (names = "-f", description = "Target file") // указываем какие опции передаем в командной строке, даем опциям имена и описание
  public String file; // file обозначили как String, т.к. jcommander не поддерживает работу с типом File

  @Parameter (names = "-d", description = "Data format") // указываем какие опции передаем в командной строке, даем опциям имена и описание
  public String format;


  public static void main (String args[]) throws IOException { //пробрасываем exception, поскольку дальше некуда - если исключение - прога ляжет, но будет информативное сообщение

    ModifiedGroupDataGenerator generator = new ModifiedGroupDataGenerator(); //создаем объект класса ModifiedGroupDataGenerator

    JCommander jCommander = new JCommander(generator); // создаем новый объект JCommander и помещаем его в локальную переменную;
    // параметр (generator) - это объект, в котором должны быть заполнены соотв. атрибуты

    try {
      jCommander.parse(args); // вызываем метод parse и передаем в кач-ве параметра args, если исключения нет - переходим к run
      //параметр (args) - это те опции (count, file), которые переданы в командной строке
    } catch (ParameterException ex) { // перехватываем ParameterException и выводим на консоль при помощи метода usage текст, который содержит инфо о доступных опциях (args: count, file)
      jCommander.usage();
      return; // поскольку запускать после исключения генератор смысла нет - return
    }

    generator.run();
  }

  private void run() throws IOException { //пробрасываем exception
    List<GroupData> groups = generateGroups(count); // генерируем группы
    if (format.equals("csv")) { // если указываем в edit config csv
      saveAsCSV(groups, new File(file)); //создаем и сохраняем файл в формате csv с группами по указанному адресу, переводим file из String в File
    } else if (format.equals("xml")) { // если указываем в edit config xml
      saveAsXML(groups, new File(file)); //создаем и сохраняем файл в формате xml с группами по указанному адресу, переводим file из String в File
    } else if (format.equals("json")) { // если указываем в edit config xml
      saveAsJson(groups, new File(file)); //создаем и сохраняем файл в формате json с группами по указанному адресу, переводим file из String в File
    } else { // если какой-то другой формат
      System.out.println("Unrecognized format" + format);
    }
  }

  private List<GroupData> generateGroups(int count) { //генерируем группы в соответствии с указанным кол-вом
    List<GroupData> groups = new ArrayList<GroupData>(); // создаем пустой список groups
    for (int i = 0; i < count; i++) { // в цикле создаем группы, добавляем в список groups (1-инициализация, 2-условие окончания, т.е выполнение пока true, 3-после каждой итерации)
      groups.add(new GroupData().withName(String.format("modified test %s", i)).withHeader(String.format("modified header %s", i)). // подстановка вместо %s порядкового номера группы
              withFooter(String.format("modified footer %s", i)));
    }
    return groups; // возвращаем список
  }

  private void saveAsCSV(List<GroupData> groups, File file) throws IOException { // пробрасываем exception в main метод
    try (Writer writer = new FileWriter(file)) { //открываем файл на запись, в кач-ве параметра передаем путь, используем try для автоматического закрытия и сохранения
      for (GroupData group : groups) { //проходимся в цикле по всем группам переменной group
        writer.write(String.format("%s; %s; %s\n", group.getName(), group.getHeader(), group.getFooter())); // записываем каждую группу в файл,
        // Вместо %s подставляется name/header/footer, затем перенос на новую строку
      }
    }
  }

  private void saveAsJson(List<GroupData> groups, File file) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create(); // создаем новый объект Gson, форматируем для красивого отображения
    // и не сериализуем те поля, которые не помечены аннотацией @Expose в GroupData
    String json = gson.toJson(groups); // сериализуем, т.е. переводим из объектного представления в строку в формате json
    // в кач-ве параметра передаем тот объект, который надо сериализовать
    try (Writer writer = new FileWriter(file)) { //открываем файл на запись, в кач-ве параметра передаем путь, используем try для автоматического закрытия и сохранения
      writer.write(json); // записываем группы в файл в формате json
    }
  }

  private void saveAsXML(List<GroupData> groups, File file) throws IOException {
    XStream xstream = new XStream(); // создаем новый объект XStream
    xstream.processAnnotations(GroupData.class); // для обработки аннотаций в классе GroupData для форматирования файла XML
    String xml = xstream.toXML(groups); // сериализуем, т.е. переводим из объектного представления в строку в формате XML
    // в кач-ве параметра передаем тот объект, который надо сериализовать
    try (Writer writer = new FileWriter(file)) {//открываем файл на запись, в кач-ве параметра передаем путь, используем try для автоматического закрытия и сохранения
      writer.write(xml); // записываем группы в файл в формате XML
    }
  }
}
