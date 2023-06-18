package ucr.rp.if7100.EmailProcessingService.events;

import java.util.Date;

public abstract class Event <T>{
    private String id;
    private String correlationalId;
    private Date date;
    private T payload;

    public Event() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorrelationalId() {
        return correlationalId;
    }

    public void setCorrelationalId(String correlationalId) {
        this.correlationalId = correlationalId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
