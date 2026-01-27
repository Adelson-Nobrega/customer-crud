package br.ada.tech.class1583.persistence;

import java.util.stream.Stream;

public interface SearchByName<T> {

    Stream<T> searchName(String name);
}
