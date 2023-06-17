package ucr.rp.if7100.EmailProcessingService.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "bank")
public class Bank {

    @Id
    private String id;

    private String name;

    protected Bank() {
        // Constructor vacío para JPA
    }

    private Bank(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class Builder {
        private String id;
        private String name;

        public Builder() {
            // Generar un ID aleatorio utilizando UUID
            this.id = UUID.randomUUID().toString();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Bank build() {
            return new Bank(id, name);
        }
    }
}