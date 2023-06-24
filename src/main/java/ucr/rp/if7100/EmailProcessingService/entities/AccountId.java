package ucr.rp.if7100.EmailProcessingService.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "accountid")
public class AccountId {

    @Id
    private String id;

    @Column(nullable = true, name = "phonenumber")
    private String phoneNumber;

    @Column(nullable = true)
    private String last4;

    @Column(nullable = true, name = "actnumber")
    private String actNumber;

    @Column(nullable = true)
    private String iban;

    public AccountId() {
        // Constructor vacío para JPA
    }

    private AccountId(String id, String phoneNumber, String last4, String actNumber, String iban) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.last4 = last4;
        this.actNumber = actNumber;
        this.iban = iban;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLast4() {
        return last4;
    }

    public String getActNumber() {
        return actNumber;
    }

    public String getIban() {
        return iban;
    }

    public static class Builder {
        private String id;
        private String phoneNumber;
        private String last4;
        private String actNumber;
        private String iban;

        public Builder() {
            // Generar un ID aleatorio utilizando UUID
            this.id = UUID.randomUUID().toString();
        }

        public Builder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder withLast4(String last4) {
            this.last4 = last4;
            return this;
        }

        public Builder withActNumber(String actNumber) {
            this.actNumber = actNumber;
            return this;
        }

        public Builder withIban(String iban) {
            this.iban = iban;
            return this;
        }

        public AccountId build() {
            return new AccountId(id, phoneNumber, last4, actNumber, iban);
        }
    }
}

