package br.ada.tech.class1583.view;

import br.ada.tech.class1583.model.Customer;
import br.ada.tech.class1583.service.EraserCustomerUseCase;
import br.ada.tech.class1583.service.RegisterCustomerUseCase;
import br.ada.tech.class1583.service.SearchCustomerUseCase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CustomerView {

    private SearchCustomerUseCase searchUseCase;
    private RegisterCustomerUseCase registerUseCase;
    private EraserCustomerUseCase eraserUseCase;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CustomerView(SearchCustomerUseCase searchUseCase, RegisterCustomerUseCase registerUseCase,
                        EraserCustomerUseCase eraserUseCase) {
        this.searchUseCase = searchUseCase;
        this.registerUseCase = registerUseCase;
        this.eraserUseCase = eraserUseCase;
    }

    public void show() {
        try (Scanner scanner = new Scanner(System.in)) {
            menu(scanner);
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Infelizmente ocorreu um erro no sistema. Tente novamente mais tarde");
        }
    }

    private void menu(Scanner scanner) {
        String option = "-1";
        do {
            System.out.println("Escolha uma opção");
            System.out.println("0 - Sair");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Consultar por id");
            System.out.println("3 - Listar todos");
            System.out.println("4 - Buscar por nome");
            System.out.println("5 - Buscar por documento");
            System.out.println("6 - Excluir cliente");
            option = scanner.nextLine();
            switch (option) {
                case "0" -> System.out.println("Até mais....");
                case "1" -> showRegister(scanner);
                case "2" -> showSearch(scanner);
                case "3" -> showAllCustomer();
                case "4" -> searchByName(scanner);
                case "5" -> searchByDocument(scanner);
                case "6" -> deleteCustomer(scanner);
                default -> System.out.println("Opção inválida! Tente novamente");
            }
        } while (!option.equalsIgnoreCase("0"));
    }

    private void showAllCustomer() {
        printHeader();
        var customers = searchUseCase.list();
        customers.forEach(this::printCustomer);
    }

    private void showSearch(Scanner scanner) {
        System.out.println("Informe o id do cliente");
        var id = Long.parseLong(scanner.nextLine());
        searchUseCase.findById(id)
                .ifPresentOrElse(customer -> {
                    printHeader();
                    printCustomer(customer);
                }, () -> System.out.println("Nenhum cliente encontrado"));
    }

    private void showRegister(Scanner scanner) {
        System.out.println("Informe o nome do cliente");
        var name = scanner.nextLine();
        System.out.println("Informe o documento do cliente");
        var document = scanner.nextLine();
        System.out.println("Informe a data de nascimento(dd/mm/yyyy)");
        var birthdate = scanner.nextLine();
        var customer = new Customer(null, name, document, LocalDate.parse(birthdate, DATE_FORMATTER));
        registerUseCase.save(customer);
        printHeader();
        printCustomer(customer);
    }

    private void printHeader() {
        System.out.print(padRight("Id", 3));
        System.out.print(" | ");
        System.out.print(padRight("Name", 20));
        System.out.print(" | ");
        System.out.print(padRight("Document", 15));
        System.out.print(" | ");
        System.out.print(padRight("Birth date", 10));
        System.out.println(" |");
    }

    private void printCustomer(Customer customer) {
        System.out.print(padRight(customer.getId().toString(), 3));
        System.out.print(" | ");
        System.out.print(padRight(customer.getName(), 20));
        System.out.print(" | ");
        System.out.print(padRight(customer.getDocument(), 15));
        System.out.print(" | ");
        System.out.print(padRight(customer.getBirthdate().format(DATE_FORMATTER), 10));
        System.out.println(" |");
    }

    private String padRight(String value, int size) {
        return String.format("%-" + size + "s", value);
    }

    public void searchByName(Scanner scanner) {
        System.out.println("Informe o nome do cliente");
        var name = scanner.nextLine();
        printHeader();
        var customers = searchUseCase.searchByName(name);
        customers.forEach(this::printCustomer);
    }

    private void searchByDocument(Scanner scanner) {
        System.out.println("Informe o documento do cliente");
        var document = scanner.nextLine();
        printHeader();
        var customers = searchUseCase.searchByDocument(document);
        if (customers.isEmpty()) {
            System.out.println("Documento não encontrado!");
        } else {
            customers.forEach(this::printCustomer);
        }
    }

    private void deleteCustomer(Scanner scanner) {
        System.out.println("Informe o id do cliente");
        var id = Long.parseLong(scanner.nextLine());
        eraserUseCase.delete(id);
    }
}