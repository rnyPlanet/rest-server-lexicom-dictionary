package com.grin.lexicom.service;

import com.grin.lexicom.model.Word;

import java.util.List;
import java.util.Optional;

public interface WordService {
    Word save(Word word);

    List<Word> findAllByDictionaryId(Long id);

    Optional<Word> findById(Long id);

    void delete(Long id);
}
