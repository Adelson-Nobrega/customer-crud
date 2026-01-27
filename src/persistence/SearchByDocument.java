package br.ada.tech.class1583.persistence;

import java.util.stream.Stream;

public interface SearchByDocument<T> {

    Stream<T> searchDocument(String document);
}
