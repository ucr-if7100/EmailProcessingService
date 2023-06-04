package ucr.rp.if7100.EmailProcessingService.entities;

import javax.persistence.*;

@Entity
@Table(name = "AccountId")
public class AccountId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = true)
    private String last4;

    @Column(nullable = true)
    private String actNumber;

    @Column(nullable = true)
    private String iban;

}