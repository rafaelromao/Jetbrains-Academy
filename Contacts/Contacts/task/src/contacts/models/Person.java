package contacts.models;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Person extends Contact {
    @Getter
    private String name;
    @Getter
    private String surname;
    @Getter
    private LocalDate birthDate;
    @Getter
    private Gender gender;

    enum Gender {
        Male,
        Female
    }

    public Person(){
        setPerson(true);
    }

    public void setSurname(String surname) {
        this.surname = surname;
        updated = LocalDateTime.now();
    }

    public void setName(String name) {
        this.name = name;
        updated = LocalDateTime.now();
    }

    public void setBirthDate(String birthDate) {
        try {
            this.birthDate = LocalDate.parse(birthDate);
        } catch (DateTimeParseException e) {
            this.birthDate = null;
        }
        updated = LocalDateTime.now();
    }

    public void setGender(String gender) {
        this.gender = "M".equals(gender)
                ? Gender.Male
                : "F".equals(gender)
                    ? Gender.Female
                    : null;
        updated = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, surname);
    }

    @Override
    public String serialize() {
        var sb = new StringBuilder();
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Surname: %s\n", surname));
        sb.append(String.format("Birth date: %s\n", birthDate != null ? birthDate : "[no data]"));
        sb.append(String.format("Gender: %s\n",
                gender == Gender.Male
                    ? "M"
                    : gender == Gender.Female
                        ? "F"
                        : "[no data]"));
        sb.append(String.format("Number: %s\n", getPhone()));
        sb.append(String.format("Time created: %s\n", created));
        sb.append(String.format("Time last edit: %s", updated));
        return sb.toString();
    }
}
