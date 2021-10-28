package com.grin.lexicom.repository;

import com.grin.lexicom.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
