package com.grin.lexicom.service.impl;

import com.grin.lexicom.model.Word;
import com.grin.lexicom.repository.WordRepository;
import com.grin.lexicom.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    @Autowired
    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public Word save(Word word) {
        return wordRepository.save(word);
    }

    @Override
    public List<Word> findAllByDictionaryId(Long id) {
        return wordRepository.findAllByDictionaryId(id);
    }

    @Override
    public Optional<Word> findById(Long id) {
        return wordRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        wordRepository.deleteById(id);
    }
}
