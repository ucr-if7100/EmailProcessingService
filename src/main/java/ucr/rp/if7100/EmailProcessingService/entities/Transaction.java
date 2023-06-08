package ucr.rp.if7100.EmailProcessingService.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Transaction")
public class Transaction {

    @Id//added
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }
}
