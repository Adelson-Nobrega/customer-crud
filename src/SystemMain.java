package br.ada.tech.class1583;

import br.ada.tech.class1583.persistence.text.CustomerFilePersistence;
import br.ada.tech.class1583.service.impl.CustomerService;
import br.ada.tech.class1583.view.CustomerView;

public class SystemMain {

    void main() {
        registerAll();
        var customerPersistence = new CustomerFilePersistence();
        var customerService = new CustomerService(
                                    customerPersistence,
                                    customerPersistence,
                                    customerPersistence,
                                    customerPersistence,
                                    customerPersistence);
        var view = new CustomerView(customerService, customerService, customerService);
        view.show();
    }

    private static void registerAll() {
        var persistence = new CustomerFilePersistence();
        DataProduces.customers()
                .forEach(persistence::save);
    }
}