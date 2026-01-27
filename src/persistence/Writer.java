package br.ada.tech.class1583.persistence;

public interface Writer<T> {

    void save(T object);

}