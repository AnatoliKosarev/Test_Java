package ru.stqa.pft.addressbook.model;

import com.google.common.collect.ForwardingSet;

import java.util.HashSet;
import java.util.Set;

public class Groups extends ForwardingSet <GroupData> {
  private Set<GroupData> delegate; //объект delegate

  public Groups(Groups groups) { // конструктор
    this.delegate = new HashSet<GroupData>(groups.delegate); // берем множество из существующего объекта, строим новое и присваиваем его в новый создаваемый этим конструктором объект (т.е. делаем копию этого множества)
  }

  public Groups() { // конструктор без параметров для метода all из GroupHelper
    this.delegate = new HashSet<GroupData>();
  }

  @Override
  protected Set<GroupData> delegate() { // метод delegate возвращает объект delegate
    return delegate;
  }

  public Groups withAdded (GroupData group) {
    Groups groups = new Groups(this); // создаем новое множество - копию delegate, с помощью конструктора
    groups.add(group); //добавляем в множество группу (метод add работает для множества, т.к. реализован в ForwardingSet)
    return groups; // возвращаем множество
  }

  public Groups without (GroupData group) {
    Groups groups = new Groups(this); // создаем новое множество - копию delegate, с помощью конструктора
    groups.remove(group); //убираем из множества группу (метод remove работает для множества, т.к. реализован в ForwardingSet)
    return groups; // возвращаем множество
  }

  public Groups withModified(GroupData modifiedGroup, GroupData group) {
    Groups groups = new Groups(this); // создаем новое множество - копию delegate, с помощью конструктора
    groups.remove(modifiedGroup); //убираем из множества модифицируемую группу из старого списка до модификации
    groups.add(group); // добавляем модифицируемую группу с соответствующим id после модификации
    return groups;
  }

}
