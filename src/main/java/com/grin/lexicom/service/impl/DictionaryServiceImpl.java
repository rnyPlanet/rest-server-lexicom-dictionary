package com.grin.lexicom.service.impl;

import com.grin.lexicom.model.Dictionary;
import com.grin.lexicom.model.User;
import com.grin.lexicom.repository.DictionaryRepository;
import com.grin.lexicom.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    @Autowired
    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public Dictionary create(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    @Override
    public Optional<Dictionary> findById(Long id) {
        return dictionaryRepository.findById(id);
    }

    @Override
    public Optional<Dictionary> findByIdAndPrivateIsNot(Long id) {
        return dictionaryRepository.findByIdAndNotPrivate(id);
    }

    @Override
    public List<Dictionary> findByNameAndUserId(String name, Long id) {
        return dictionaryRepository.getByNameAndUserId(name, id);
    }

    @Override
    public List<Dictionary> findAll() {
        return dictionaryRepository.findAll();
    }

    @Override
    public List<Dictionary> findAllByPrivateIsNot() {
        return dictionaryRepository.whereNotPrivate();
    }

    @Override
    public List<Dictionary> findMy(Long id) {
        return dictionaryRepository.my(id);
    }

    @Override
    public Dictionary findByIdAndNotPrivate(Long id) {
        return dictionaryRepository.findByIdAndNotPrivate(id).orElse(null);
    }

    @Override
    public List<Dictionary> findByUserId(Long id) {
        return dictionaryRepository.findByUserId(id);
    }

    @Override
    public Dictionary findByUserIdAndDictId(Long userId, Long dictId) {
        return dictionaryRepository.findByUserIdAndDictId(userId, dictId);
    }

    @Override
    public void delete(Long id) {
        dictionaryRepository.deleteById(id);
    }

    @Override
    public Dictionary update(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    @Override
    public List<Dictionary> findMySubscriptions(Long id) {
        return dictionaryRepository.mySubscriptions(id);
    }

    @Override
    public boolean isUserSubscribedById(long idDictionary, long idUser) {
        return dictionaryRepository.isUserSubscribedById(idDictionary, idUser);
    }

    @Override
    public void unsubscribe(long idDictionary, long idUser) {
        dictionaryRepository.unsubscribe(idDictionary, idUser);
    }

    @Override
    public Dictionary findMySubscriptionByDictIdAndUserId(Long idDictionary, Long idUser) {
        return dictionaryRepository.mySubscriptionByDictIdAndUserId(idDictionary, idUser);
    }


    @Override
    public void subscribe(long idDictionary, long idUser) {
        dictionaryRepository.subscribe(idDictionary, idUser);
    }

}
