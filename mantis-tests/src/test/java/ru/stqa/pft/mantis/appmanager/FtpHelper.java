package ru.stqa.pft.mantis.appmanager;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FtpHelper {


  private final ApplicationManager app;
  private FTPClient ftp;

  public FtpHelper(ApplicationManager app) {
    this.app = app; // инициализируем ссылку на ApplicationManager
    ftp = new FTPClient(); //инициализируется FTPClient, который будет создавать соединение, передавать файлы и выполнять другие действие
  }

    public void upload (File file, String target, String backup) throws IOException { // в этом методе подменяем старый конфиг. файл новым путем переименования
    ftp.connect(app.getProperty("ftp.host")); // заходим на сервер
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password")); // логинимся
    ftp.deleteFile(backup); // удаляем резервную копию если есть
    ftp.rename(target, backup); // делаем новую резервную копию
    ftp.enterLocalPassiveMode(); // тех. манипуляция
    ftp.storeFile(target, new FileInputStream(file)); // передаем новый конфиг файл
    ftp.disconnect();
    }

    public void restore(String backup, String target) throws IOException { // в этом методе восстанавливаем все как было, т.е. удаляем target и воссатнавливаем backup
      ftp.connect(app.getProperty("ftp.host"));
      ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
      ftp.deleteFile(target); // удаляем переданный конфиг файл
      ftp.rename(backup, target); // восстанавливаем оригинальный конфиг файл из резервной копии
      ftp.disconnect();
    }
}
