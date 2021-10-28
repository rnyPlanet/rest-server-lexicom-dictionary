package com.grin.lexicom.service;

import com.grin.lexicom.model.Language;

import java.util.List;
import java.util.Optional;

public interface LanguageService {
    List<Language> findAll();

    Optional<Language> findById(Long id);

}
