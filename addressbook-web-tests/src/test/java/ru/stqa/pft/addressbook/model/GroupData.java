package ru.stqa.pft.addressbook.model;

import java.util.Objects;

public class GroupData {

  private int id = Integer.MAX_VALUE;
  private String name;
  private String header;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GroupData groupData = (GroupData) o;
    return id == groupData.id &&
            Objects.equals(name, groupData.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
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
