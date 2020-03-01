package contacts.services;

import contacts.models.Contact;
import contacts.models.Person;

import java.util.Scanner;

public class PersonService {
    static void editPerson(Person contact, Scanner scanner) {
        System.out.println("Select a field (name, surname, number):");
        var field = scanner.nextLine();
        switch (field) {
            case "name":
                System.out.println("Enter the name:");
                contact.setName(scanner.nextLine());
                break;
            case "surname":
                System.out.println("Enter the surname:");
                contact.setSurname(scanner.nextLine());
                break;
            case "birth date":
                System.out.println("Enter the birth date:");
                contact.setBirthDate(scanner.nextLine());
                if (contact.getBirthDate() == null)
                    System.out.println("Bad birth date!");
                break;
            case "gender":
                System.out.println("Enter the gender (M, F):");
                contact.setGender(scanner.nextLine());
                if (contact.getGender() == null)
                    System.out.println("Bad gender!");
                break;
            case "number":
                System.out.println("Enter the number:");
                contact.setPhone(scanner.nextLine());
                break;
        }
    }

    static Contact enterPerson(Scanner scanner) {
        var contact = new Person();
        System.out.println("Enter the name for the person:");
        contact.setName(scanner.nextLine());
        System.out.println("Enter the surname for the person:");
        contact.setSurname(scanner.nextLine());
        System.out.println("Enter the birth date:");
        contact.setBirthDate(scanner.nextLine());
        if (contact.getBirthDate() == null)
            System.out.println("Bad birth date!");
        System.out.println("Enter the gender (M, F):");
        contact.setGender(scanner.nextLine());
        if (contact.getGender() == null)
            System.out.println("Bad gender!");
        System.out.println("Enter the number:");
        contact.setPhone(scanner.nextLine());
        return contact;
    }
}
