package contacts;

import contacts.models.Contact;

import java.io.IOException;
import java.util.Scanner;

import static contacts.services.ContactService.*;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length > 1 && args[0] == "open") {
            openContacts(args[1]);
        }
        try (var scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("[menu] Enter action (add, list, count, search, exit):");
                var option = scanner.nextLine();
                switch (option) {
                    case "add":
                        addContact(enterContact(scanner));
                        break;
                    case "list":
                        list(scanner);
                        break;
                    case "count":
                        countContacts();
                        break;
                    case "search":
                        search(scanner);
                        break;
                    case "exit":
                        return;
                }
                System.out.println();
            }
        }
    }

    private static void list(Scanner scanner) throws IOException {
        listContacts();
        System.out.println();
        System.out.print("[list] Enter action ([number], back):");
        var option = scanner.nextLine();
        switch (option) {
            case "back":
                return;
            default:
                record(scanner, Integer.parseInt(option));
        }
    }

    private static void search(Scanner scanner) throws IOException {
        searchContacts(scanner);
        System.out.println();
        System.out.print("[search] Enter action ([number], back, again):");
        var option = scanner.nextLine();
        switch (option) {
            case "again":
                search(scanner);
                break;
            case "back":
                return;
            default:
                record(scanner, Integer.parseInt(option));
        }
    }

    private static void record(Scanner scanner, int contactIndex) throws IOException {
        var contact = selectContact(contactIndex);
        record(scanner, contact);
    }

    private static void record(Scanner scanner, Contact contact) throws IOException {
        showContact(contact);
        System.out.println();
        System.out.print("[record] Enter action (edit, delete, menu):");
        var option = scanner.nextLine();
        switch (option) {
            case "edit":
                editContact(contact, scanner);
                record(scanner, contact);
                break;
            case "delete":
                removeContact(contact);
                break;
            case "menu":
                return;
        }
    }
}

