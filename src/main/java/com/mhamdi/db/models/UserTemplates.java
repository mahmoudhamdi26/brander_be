package com.mhamdi.db.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "users_templates")
public class UserTemplates implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
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
