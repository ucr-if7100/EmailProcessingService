package ucr.rp.if7100.EmailProcessingService.entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Email {
    @Id
    private String id;
    private String subject;
    private String fromAddress;
    private String toAddress;
    private String sentDate;
    private String content;

    public Email() { }

    public Email(String id, String subject, String fromAddress, String toAddress, String sentDate, String content) {
        this.id = id;
        this.subject = subject;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.sentDate = sentDate;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", sentDate='" + sentDate + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public static class Builder {
        private String id;
        private String subject;
        private String fromAddress;
        private String toAddress;
        private String sentDate;
        private String content;

        public Builder() {
            this.id = UUID.randomUUID().toString();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
            return this;
        }

        public Builder withToAddress(String toAddress) {
            this.toAddress = toAddress;
            return this;
        }

        public Builder withSentDate(String sentDate) {
            this.sentDate = sentDate;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Email build() {
            return new Email(this.id, this.subject, this.fromAddress, this.toAddress, this.sentDate, this.content);
        }
    }
}
