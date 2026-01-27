package br.ada.tech.class1583.persistence;

import java.util.Optional;
import java.util.stream.Stream;

public interface Reader<T> {

    Optional<T> read(Long id);

    Stream<T> read();

}