package contacts.services;

import contacts.models.Contact;
import contacts.models.Organization;
import contacts.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static contacts.services.OrganizationService.*;
import static contacts.services.PersonService.*;

public class ContactService {

    private static final List<Contact> contacts = new ArrayList<>();

    public static boolean showContact(Contact contact) {
        if (contact == null) return false;
        var data = contact.serialize();
        System.out.println(data);
        return true;
    }

    public static void listContacts() {
        for (int i = 0; i < contacts.size(); i++) {
            var contact = contacts.get(i);
            System.out.printf("%d. %s\n", i+1, contact);
        }
    }

    public static void countContacts() {
        System.out.printf("The Phone Book has %d records.\n", contacts.size());
    }

    public static boolean editContact(Contact contact, Scanner scanner) {
        if (contact == null) return false;
        if (contact.isPerson()) {
            editPerson((Person)contact, scanner);
        } else {
            editOrganization((Organization)contact, scanner);
        }
        System.out.println("The record updated.");
        return true;
    }

    public static Contact selectContact(Scanner scanner) {
        if (contacts.size() == 0) {
            return null;
        }
        System.out.println("Select a record:");
        var index = scanner.nextInt();
        scanner.nextLine();
        return contacts.get(index-1);
    }

    public static void addContact(Contact contact) {
        if (contact == null) return;
        contacts.add(contact);
        System.out.println("The record added.");
    }

    public static boolean removeContact(Contact contact) {
        if (contact == null) return false;
        contacts.remove(contact);
        System.out.println("The record removed.");
        return true;
    }

    public static Contact enterContact(Scanner scanner) {
        System.out.println("Enter the type (person, organization):");
        var type = scanner.nextLine();
        Contact contact = null;
        switch (type) {
            case "person":
                contact = enterPerson(scanner);
                break;
            case "organization":
                contact = enterOrganization(scanner);
                break;
        }
        return contact;
    }
}
