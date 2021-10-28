package com.grin.lexicom.service.impl;

import com.grin.lexicom.model.Language;
import com.grin.lexicom.repository.LanguageRepository;
import com.grin.lexicom.service.LanguageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<Language> findAll() {
        return languageRepository.findAll();
    }

    @Override
    public Optional<Language> findById(Long id) {
        return languageRepository.findById(id);
    }


}
