package contacts.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public abstract class Contact {
    @Getter
    private String phone;
    @Getter @Setter
    private boolean isPerson;

    protected LocalDateTime created;
    protected LocalDateTime updated;

    public Contact() {
        created = LocalDateTime.now();
        phone = "";
    }

    public void setPhone(String phone) {
        if (validatePhone(phone)) {
            this.phone = phone;
        } else {
            this.phone = "";
        }
        updated = LocalDateTime.now();
    }

    private boolean validatePhone(String phone) {
        final String regex = "^(?:(?:^\\+?[0-9a-zA-Z]{0,}){1,})?(?:[ \\-]?\\(?[0-9a-zA-Z]{2,}\\)?)?(?:[ \\-]{1}[0-9a-zA-Z]{2,}){0,}$";
        return phone.matches(regex);
    }

    public abstract String serialize();
}
