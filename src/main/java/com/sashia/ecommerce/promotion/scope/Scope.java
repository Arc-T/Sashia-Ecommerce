package com.sashia.ecommerce.promotion.scope;

import com.sashia.ecommerce.promotion.condition.type.ConditionType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

//What is being evaluated?

@Entity
@Table(name = "scopes", schema = "promotion")
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ScopeCode code;

    private String title;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /* ******************************* TABLE RELATIONS ******************************** */

    @ManyToMany
    @JoinTable(
            name = "promotion_level_condition_types",
            schema = "promotion",
            joinColumns = @JoinColumn(name = "promotion_level_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_condition_type_id")
    )
    private Set<ConditionType> conditionTypes = new LinkedHashSet<>();

    /* ****************************** GETTER & SETTERS ******************************** */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScopeCode getCode() {
        return code;
    }

    public void setCode(ScopeCode code) {
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

    public Set<ConditionType> getConditionTypes() {
        return conditionTypes;
    }

    public void setConditionTypes(Set<ConditionType> conditionTypes) {
        this.conditionTypes = conditionTypes;
    }

}