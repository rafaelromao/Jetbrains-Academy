package contacts;

import java.util.Scanner;

import static contacts.services.ContactService.*;

public class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Enter action (add, remove, info, edit, count, list, exit):");
                var option = scanner.nextLine();
                switch (option) {
                    case "add":
                        addContact(enterContact(scanner));
                        break;
                    case "remove":
                        if (!removeContact(selectContact(scanner)))
                            System.out.println("No records to remove");
                        break;
                    case "info":
                        if (!showContact(selectContact(scanner)));
                            System.out.println("No records to show");
                        break;
                    case "edit":
                        if (!editContact(selectContact(scanner), scanner));
                            System.out.println("No records to edit");
                        break;
                    case "count":
                        countContacts();
                        break;
                    case "list":
                        listContacts();
                        break;
                    case "exit":
                        return;
                }
                System.out.println(" ");
            }
        }
    }
}

