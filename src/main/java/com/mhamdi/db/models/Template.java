package com.mhamdi.db.models;

import com.mhamdi.brander.apis.dto.response.DataInterface;
import com.mhamdi.db.models.enums.AccountType;
import com.mhamdi.db.models.enums.TemplateType;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "templates")
public class Template implements DataInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = true)
    private String title;
    @Lob
    @Column(nullable = true)
    private String descr;
    @Enumerated(EnumType.STRING)
    private TemplateType type;
    @Column(nullable = true)
    private String logo;
    @Column(nullable = true)
    private String cover;
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
    

    @OneToMany(mappedBy = "template")
    Set<UserTemplates> users;
}
