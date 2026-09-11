package com.sashia.ecommerce.promotion.condition.type;

import com.sashia.ecommerce.promotion.scope.Scope;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "condition_types", schema = "promotion")
public class ConditionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConditionTypeCode code;

    private String title;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /* ******************************* TABLE RELATIONS ******************************** */

    @ManyToMany(mappedBy = "conditionTypes")
    private Set<Scope> levels = new LinkedHashSet<>();

    /* ****************************** GETTER & SETTERS ******************************** */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConditionTypeCode getCode() {
        return code;
    }

    public void setCode(ConditionTypeCode code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Scope> getLevels() {
        return levels;
    }

    public void setLevels(Set<Scope> levels) {
        this.levels = levels;
    }

}
