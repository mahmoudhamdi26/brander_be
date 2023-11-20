package com.mhamdi.db.repos;

import com.mhamdi.db.models.UserTemplates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTemplatesRepository extends JpaRepository<UserTemplates, Long> {
}