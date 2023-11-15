package com.mhamdi.db.models;

import lombok.*;

import com.mhamdi.db.models.keys.UserTemplateCompositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_templates")
public class UserTemplates {
    // @EmbeddedId
    // UserTemplateCompositeKey id;
    @Id     
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "NUMERIC(19,0)")
    Long id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("templateId")
    @JoinColumn(name = "template_id")
    Template template;

    String title;
    // File
    @Column(nullable = false)
    private String fileid;
    @Column(nullable = false)
    private String filepath;
    @Column(nullable = false)
    private String filename;
    @Column(nullable = false)
    private String filetype;
    @Column(nullable = false)
    private Long filesize;
}
