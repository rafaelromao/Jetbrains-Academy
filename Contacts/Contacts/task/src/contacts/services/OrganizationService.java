package contacts.services;

import contacts.models.Contact;
import contacts.models.Organization;

import java.util.Scanner;

public class OrganizationService {
    static void editOrganization(Organization contact, Scanner scanner) {
        System.out.println("Select a field (organization name, address, number):");
        var field = scanner.nextLine();
        switch (field) {
            case "organization name":
                System.out.println("Enter the organization name:");
                contact.setOrganizationName(scanner.nextLine());
                break;
            case "address":
                System.out.println("Enter the address:");
                contact.setAddress(scanner.nextLine());
                break;
            case "number":
                System.out.println("Enter the number:");
                contact.setPhone(scanner.nextLine());
                break;
        }
    }

    static Contact enterOrganization(Scanner scanner) {
        var contact = new Organization();
        System.out.println("Enter the organization name:");
        contact.setOrganizationName(scanner.nextLine());
        System.out.println("Enter the address:");
        contact.setAddress(scanner.nextLine());
        System.out.println("Enter the number:");
        contact.setPhone(scanner.nextLine());
        return contact;
    }
}
