package br.ada.tech.class1583.service.impl;

import br.ada.tech.class1583.model.Customer;
import br.ada.tech.class1583.persistence.Reader;
import br.ada.tech.class1583.persistence.SearchByDocument;
import br.ada.tech.class1583.persistence.SearchByName;
import br.ada.tech.class1583.persistence.Writer;
import br.ada.tech.class1583.service.RegisterCustomerUseCase;
import br.ada.tech.class1583.service.SearchCustomerUseCase;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CustomerService implements RegisterCustomerUseCase, SearchCustomerUseCase {

    private Writer<Customer> customerWriter;
    private Reader<Customer> customerReader;
    private SearchByName<Customer> customerSearchByName;
    private SearchByDocument<Customer> customerSearchByDocument;

    public CustomerService(Writer<Customer> customerWriter,
                           Reader<Customer> customerReader,
                           SearchByName<Customer> customerSearchByName,
                           SearchByDocument<Customer> customerSearchByDocument) {
        this.customerWriter = customerWriter;
        this.customerReader = customerReader;
        this.customerSearchByName = customerSearchByName;
        this.customerSearchByDocument = customerSearchByDocument;
    }

    @Override
    public void save(Customer customer) {
        if (customer == null) {
            throw new IllegalStateException("Customer não pode ser nulo");
        }
        customerWriter.save(customer);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        if (id == null) {
            throw new IllegalStateException("Id não pode ser nulo");
        }
        return customerReader.read(id);
    }

    @Override
    public List<Customer> list() {
        return customerReader.read()
                .sorted(Comparator.comparing(Customer::getId))
                .toList();
    }

    @Override
    public List<Customer> searchByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Nome não pode ser nulo ou vazio");
        }
        return customerSearchByName.searchName(name)
                .sorted(Comparator.comparing(Customer::getName))
                .toList();
    }

    @Override
    public List<Customer> searchByDocument(String document) {
        if (document == null || document.isBlank()) {
            throw new IllegalStateException("Documento não pode ser nulo ou vazio");
        }
        return customerSearchByDocument.searchDocument(document)
                .sorted(Comparator.comparing(Customer::getDocument))
                .toList();
    }

}