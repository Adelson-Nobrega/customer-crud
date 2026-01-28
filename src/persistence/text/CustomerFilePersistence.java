package br.ada.tech.class1583.persistence.text;

import br.ada.tech.class1583.model.Customer;
import br.ada.tech.class1583.persistence.*;

import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class CustomerFilePersistence extends TextFile
        implements Writer<Customer>, Reader<Customer>,
                   SearchByName<Customer>, SearchByDocument<Customer>, Eraser<Customer> {

    private static final String ROOT_FOLDER = "./database/customers";
    private static final Function<String, Customer> READ_CONVERTER = (fileContent) -> {
        var fields = fileContent.split(CONTENT_SEPARATOR);
        var id = Long.parseLong(fields[0]);
        var name = fields[1];
        var document = fields[2];
        var birthdate = LocalDate.parse(fields[3]);
        return new Customer(id, name, document, birthdate);
    };

    private static final Function<Customer, String> WRITE_CONVERTER = (customer) -> {
        return customer.getId() + CONTENT_SEPARATOR
                + customer.getName() + CONTENT_SEPARATOR
                + customer.getDocument() + CONTENT_SEPARATOR
                + customer.getBirthdate() + CONTENT_SEPARATOR;
    };

    public CustomerFilePersistence() {
        super(ROOT_FOLDER);
    }

    @Override
    public Optional<Customer> read(Long id) {
        return getFile(id.toString())
                .filter(Files::exists)
                .map(this::readContent)
                .map(READ_CONVERTER);
    }

    @Override
    public Stream<Customer> read() {
        return filesInRootFolder()
                .map(this::readContent)
                .map(READ_CONVERTER);
    }

    @Override
    public void save(Customer customer) {
        fixId(customer);
        var content = WRITE_CONVERTER.apply(customer);
        var fileName = customer.getId().toString();
        var file = getFile(fileName)
                .filter(Files::exists)
                .orElse(createFile(fileName));
        write(file, content);
    }

    private void fixId(Customer customer) {
        if (customer.getId() == null) {
            var id = nextId().orElse(1l);
            customer.setId(id);
        }
    }

    private Optional<Long> nextId() {
        return filesInRootFolder()
                .map(file -> file.getFileName().toString())
                .map(fileName -> fileName.replaceAll(EXTENSION, ""))
                .map(Long::parseLong)
                .max(Long::compareTo)
                .map(it -> it + 1);
    }

    @Override
    public Stream<Customer> searchName(String name) {
        return read()
                .filter(customer -> customer.getName().toLowerCase()
                                                      .contains(name.toLowerCase()));
    }

    @Override
    public Stream<Customer> searchDocument(String document) {
        return read()
                .filter(customer -> customer.getDocument().equals(document));
    }

    @Override
    public void delete(Long id) {
        super.deleteFile(id.toString());
    }
}