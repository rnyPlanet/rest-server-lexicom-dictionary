package com.grin.lexicom.service;

import com.grin.lexicom.model.Dictionary;
import com.grin.lexicom.model.User;

import java.util.List;
import java.util.Optional;

public interface DictionaryService {
    Dictionary create(Dictionary dictionary);

    Optional<Dictionary> findById(Long id);

    Optional<Dictionary> findByIdAndPrivateIsNot(Long id);

    List<Dictionary> findByNameAndUserId(String name, Long id);

    List<Dictionary> findAll();

    List<Dictionary> findAllByPrivateIsNot();

    List<Dictionary> findMy(Long id);

    Dictionary findByIdAndNotPrivate(Long id);

    List<Dictionary> findByUserId(Long id);

    Dictionary findByUserIdAndDictId(Long userId, Long dictId);

    void delete(Long id);

    Dictionary update(Dictionary dictionary);


    List<Dictionary> findMySubscriptions(Long id);

    boolean isUserSubscribedById(long idDictionary, long idUser);

    void subscribe(long idDictionary, long idUser);

    void unsubscribe(long idDictionary, long idUser);

    Dictionary findMySubscriptionByDictIdAndUserId(Long idDictionary, Long idUser);





}
