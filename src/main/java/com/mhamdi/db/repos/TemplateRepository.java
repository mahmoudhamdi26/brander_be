package com.mhamdi.db.repos;

import com.mhamdi.db.models.Template;
import com.mhamdi.db.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Integer> {
    
}
