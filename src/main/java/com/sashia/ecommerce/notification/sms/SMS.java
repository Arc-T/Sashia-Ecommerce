package com.sashia.ecommerce.notification.sms;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms", schema = "notification")
public class SMS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    private String text;

    private String response;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /* **************************** FOREIGN-KEY RELATIONS **********************************/

    @ManyToOne(fetch = FetchType.LAZY)
    private SMSStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private SMSTemplate template;

    /* **************************** GETTER & SETTERS **********************************/

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String phone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String text() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String response() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SMSStatus status() {
        return status;
    }

    public void setStatus(SMSStatus status) {
        this.status = status;
    }

    public SMSTemplate template() {
        return template;
    }

    public void setTemplate(SMSTemplate template) {
        this.template = template;
    }

}
