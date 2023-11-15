package com.mhamdi.db.models.keys;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class UserTemplateCompositeKey implements Serializable {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "template_id")
    Integer templateId;
}
