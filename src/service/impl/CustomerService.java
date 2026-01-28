package br.ada.tech.class1583.service.impl;

import br.ada.tech.class1583.model.Customer;
import br.ada.tech.class1583.persistence.*;
import br.ada.tech.class1583.service.EraserCustomerUseCase;
import br.ada.tech.class1583.service.RegisterCustomerUseCase;
import br.ada.tech.class1583.service.SearchCustomerUseCase;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CustomerService implements RegisterCustomerUseCase, SearchCustomerUseCase,
        EraserCustomerUseCase {

    private Writer<Customer> customerWriter;
    private Reader<Customer> customerReader;
    private SearchByName<Customer> customerSearchByName;
    private SearchByDocument<Customer> customerSearchByDocument;
    private Eraser<Customer> customerEraser;

    public CustomerService(Writer<Customer> customerWriter,
                           Reader<Customer> customerReader,
                           SearchByName<Customer> customerSearchByName,
                           SearchByDocument<Customer> customerSearchByDocument,
                           Eraser<Customer> customerEraser){
        this.customerWriter = customerWriter;
        this.customerReader = customerReader;
        this.customerSearchByName = customerSearchByName;
        this.customerSearchByDocument = customerSearchByDocument;
        this.customerEraser = customerEraser;
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

    @Override
    public void delete(Long id) {
        var customer = findById(id);
        if (customer.isEmpty()) {
            System.out.println("ID do cliente não encontrado!");
        }
        customerEraser.delete(id);
    }
}