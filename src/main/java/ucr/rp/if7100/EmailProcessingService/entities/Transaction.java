package ucr.rp.if7100.EmailProcessingService.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private Date date;

    private float amount;

    private String reference;

    private String description;

    private String category;

    private boolean isExpense;

    @ManyToOne
    @JoinColumn(name = "Bank_id", nullable = false)
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "AccountId_id", nullable = false)
    private AccountId accountId;
}
