package contacts.models;

import lombok.Getter;

import java.time.LocalDateTime;

public class Organization extends Contact {
    @Getter
    private String organizationName;
    @Getter
    private String address;

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
        updated = LocalDateTime.now();
    }

    public void setAddress(String address) {
        this.address = address;
        updated = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("%s %s", organizationName, address);
    }

    @Override
    public String serialize() {
        var sb = new StringBuilder();
        sb.append(String.format("Organization name: %s\n", organizationName));
        sb.append(String.format("Address: %s\n", address));
        sb.append(String.format("Number: %s\n", getPhone()));
        sb.append(String.format("Time created: %s\n", created));
        sb.append(String.format("Time last edit: %s", updated));
        return sb.toString();
    }
}
