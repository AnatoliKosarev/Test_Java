package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.stqa.pft.addressbook.model.ContactData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactDataGenerator {

  @Parameter (names = "-c", description = "Contact count") // указываем какие опции передаем в командной строке, даем опциям имена и описание
  public int count;

  @Parameter (names = "-f", description = "Target file") // указываем какие опции передаем в командной строке, даем опциям имена и описание
  public String file; // file обозначили как String, т.к. jcommander не поддерживает работу с типом File

  public static void main(String args[]) throws IOException { //пробрасываем exception, поскольку дальше некуда - если исключение - прога ляжет, но будет информативное сообщение

    ContactDataGenerator generator = new ContactDataGenerator(); //создаем объект класса ContactDataGenerator

    JCommander jCommander = new JCommander(generator); // создаем новый объект JCommander и помещаем его в локальную переменную;

    try {
      jCommander.parse(args); // вызываем метод parse и передаем в кач-ве параметра args, если исключения нет - переходим к run
      // параметр (args) - это те опции (count, file), которые переданы в командной строке
    }catch (ParameterException ex) { // перехватываем ParameterException и выводим на консоль при помощи метода usage текст, который содержит инфо о доступных опциях (args: count, file)
      jCommander.usage();
      return; // поскольку запускать после исключения генератор смысла нет - return
    }
    generator.run();
  }

  private void run() throws IOException {
    List<ContactData> contacts = generateContacts(count); // генерируем контакты
    saveAsJson(contacts, new File(file)); //создаем и сохраняем файл в формате json с контактами по указанному адресу, переводим file из String в File
  }

  private List<ContactData> generateContacts(int count) { //генерируем контакты в соответствии с указанным кол-вом
    List<ContactData> contacts = new ArrayList<>(); // создаем пустой список contacts
    for (int i = 0; i < count; i++) { // в цикле создаем контакты, добавляем в список contacts (1-инициализация, 2-условие окончания, т.е выполнение пока true, 3-после каждой итерации)
      contacts.add(new ContactData().withFirstname(String.format("test name %s", i)).withLastname(String.format("test last name %s", i)).
              withAddress(String.format("City %s, Str. %s, Bl. %s, App. %s", i,i,i,i)).withHomePhone(String.format("123%s", i)).withMobilePhone(String.format("456%s", i)).
              withWorkPhone(String.format("789%s", i)).withEmail(String.format("test_ignore%s@test.com", i)).withGroupName("test1"));
    }
    return contacts; // возвращаем список
  }

  private void saveAsJson(List<ContactData> contacts, File file) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create(); // создаем новый объект Gson, форматируем для красивого отображения
    // и не сериализуем те поля, которые не помечены аннотацией @Expose в ContactData
    String json = gson.toJson(contacts); // сериализуем, т.е. переводим из объектного представления в строку в формате json
    // в кач-ве параметра передаем тот объект, который надо сериализовать
    try (Writer writer = new FileWriter(file)){ //открываем файл на запись, в кач-ве параметра передаем путь, используем try для автоматического закрытия и сохранения
      writer.write(json); // записываем контакты в файл в формате json
    }
  }
}
