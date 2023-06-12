package ucr.rp.if7100.EmailProcessingService.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "Transaction")
public class Transaction {

    @Id
    private String id;

    private String email;

    private Date date;

    private float amount;

    private String reference;

    private String description;

    private String category;

    @Column(name = "isexpense")
    private boolean isExpense;

    @ManyToOne
    @JoinColumn(name = "Bank_id", nullable = false)
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "Accountid_id", nullable = false)
    private AccountId accountId;

    protected Transaction() {
        // Constructor vacío para JPA
    }

    private Transaction(String id, String email, Date date, float amount, String reference, String description, String category, boolean isExpense, Bank bank, AccountId accountId) {
        this.id = id;
        this.email = email;
        this.date = date;
        this.amount = amount;
        this.reference = reference;
        this.description = description;
        this.category = category;
        this.isExpense = isExpense;
        this.bank = bank;
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Date getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }

    public String getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public Bank getBank() {
        return bank;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public static class Builder {
        private String id;
        private String email;
        private Date date;
        private float amount;
        private String reference;
        private String description;
        private String category;
        private boolean isExpense;
        private Bank bank;
        private AccountId accountId;

        public Builder() {
            // Generar un ID aleatorio utilizando UUID
            this.id = UUID.randomUUID().toString();
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder withAmount(float amount) {
            this.amount = amount;
            return this;
        }

        public Builder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder withIsExpense(boolean isExpense) {
            this.isExpense = isExpense;
            return this;
        }

        public Builder withBank(Bank bank) {
            this.bank = bank;
            return this;
        }

        public Builder withAccountId(AccountId accountId) {
            this.accountId = accountId;
            return this;
        }

        public Transaction build() {
            return new Transaction(id, email, date, amount, reference, description, category, isExpense, bank, accountId);
        }
    }
}