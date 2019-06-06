package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@XStreamAlias("group") // для генератора, чтобы в файле тэг для GroupData вместо длинного названия пакета был просто group

@Entity // для привязки к БД для Hibernate
@Table(name = "group_list") // для привязки к таблице БД для Hibernate

public class GroupData {

  @XStreamOmitField // для генератора, чтобы в файле не было id, т.к. они все одинаковые (max value)
  @Id // для привязки к таблице БД для Hibernate - т.к. id используется как идентификатор, поэтому ему присваивается особая аннотация
  @Column(name = "group_id") // для привязки к столбцу в таблице БД для Hibernate
  private int id = Integer.MAX_VALUE;

  @Expose //для сериализации в генераторе json
  @Column(name = "group_name") // для привязки к таблице БД для Hibernate
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GroupData groupData = (GroupData) o;
    return id == groupData.id &&
            Objects.equals(name, groupData.name) &&
            Objects.equals(header, groupData.header) &&
            Objects.equals(footer, groupData.footer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, header, footer);
  }

  @Expose //для сериализации в генераторе json
  @Column(name = "group_header") // для привязки к таблице БД для Hibernate
  @Type(type = "text") // описание типа для преобразования для Hibernate
  private String header;

  @Expose //для сериализации в генераторе json
  @Column(name = "group_footer") // для привязки к таблице БД для Hibernate
  @Type(type = "text") // описание типа для преобразования для Hibernate
  private String footer;

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public String getHeader() {
    return header;
  }

  public String getFooter() {
    return footer;
  }

  public GroupData withId(int id) { //сеттер, возвращает тот объект в котором вызывался, после модификации
    this.id = id;
    return this;
  }

  public GroupData withName(String name) { //сеттер, возвращает тот объект в котором вызывался, после модификации
    this.name = name;
    return this;
  }

  public GroupData withHeader(String header) { //сеттер, возвращает тот объект в котором вызывался, после модификации
    this.header = header;
    return this;
  }

  public GroupData withFooter(String footer) { //сеттер, возвращает тот объект в котором вызывался, после модификации
    this.footer = footer;
    return this;
  }

  @Override
  public String toString() {
    return "GroupData{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", header='" + header + '\'' +
            ", footer='" + footer + '\'' +
            '}';
  }

}
