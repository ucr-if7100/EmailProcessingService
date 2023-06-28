package ucr.rp.if7100.EmailProcessingService.entities;

import ucr.rp.if7100.EmailProcessingService.enums.TransactionType;

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

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "transactiontype")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Accountid_id", nullable = false)
    private AccountId accountId;

    @Column(name = "read_status")
    private boolean readStatus;

    protected Transaction() {
        // Constructor vacío para JPA
    }

    private Transaction(String id, String email, Date date, float amount, String reference, String description, String category, String bankName, TransactionType transactionType, AccountId accountId, boolean readStatus) {
        this.id = id;
        this.email = email;
        this.date = date;
        this.amount = amount;
        this.reference = reference;
        this.description = description;
        this.category = category;
        this.bankName = bankName;
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.readStatus = readStatus;
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

    public String getBankName(){return bankName;}

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public static class Builder {
        private String id;
        private String email;
        private Date date;
        private float amount;
        private String reference;
        private String description;
        private String category;
        private String bankName;
        private TransactionType transactionType;
        private AccountId accountId;
        private boolean readStatus;

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

        public Builder withBankName(String bankName) {
            this.bankName = bankName;
            return this;
        }


        public Builder withTransactionType(TransactionType isExpense) {
            this.transactionType = isExpense;
            return this;
        }


        public Builder withAccountId(AccountId accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder withRead(boolean readStatus) {
            this.readStatus = readStatus;
            return this;
        }

        public Transaction build() {
            return new Transaction(id, email, date, amount, reference, description, category, bankName, transactionType, accountId, readStatus);
        }
    }
}
