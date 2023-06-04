package ucr.rp.if7100.EmailProcessingService.entities;

import javax.persistence.*;

@Entity
@Table(name = "Bank")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}