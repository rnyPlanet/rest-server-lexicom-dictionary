package com.grin.lexicom.repository;

import com.grin.lexicom.model.Dictionary;
import com.grin.lexicom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
    @Query("SELECT d FROM Dictionary d WHERE d.user.id = :id")
    List<Dictionary> my(Long id);

    @Query(value = "select * FROM dictionary WHERE is_private = false", nativeQuery = true)
    List<Dictionary> whereNotPrivate();

    List<Dictionary> getByNameAndUserId(String name, Long id);

    @Query(value = "select * FROM dictionary WHERE is_private = false and id = :id", nativeQuery = true)
    Optional<Dictionary> findByIdAndNotPrivate(Long id);

    List<Dictionary> findByUserId(Long id);

    @Query(value = "select * FROM dictionary WHERE id_user = :userId and id = :dictId", nativeQuery = true)
    Dictionary findByUserIdAndDictId(Long userId, Long dictId);


    @Query(value = "select * from dictionary where id IN (SELECT id_dictionary FROM dictionary_users where id_user = :id);", nativeQuery = true)
    @Transactional
    List<Dictionary> mySubscriptions(@Param("id") Long id);

    @Query(value = "select * from dictionary where id IN (SELECT id_dictionary FROM dictionary_users where id_user = :id_user and id_dictionary = :id_dictionary);", nativeQuery = true)
    @Transactional
    Dictionary mySubscriptionByDictIdAndUserId(@Param("id_dictionary") long idDictionary, @Param("id_user") long idUser);

    @Modifying
    @Query(value = "insert into dictionary_users (id_dictionary, id_user) VALUES (:id_dictionary, :id_user)", nativeQuery = true)
    @Transactional
    void subscribe(@Param("id_dictionary") long idDictionary, @Param("id_user") long idUser);

    @Modifying
    @Transactional
    @Query(value = "delete from dictionary_users where id_dictionary = :idDictionary and id_user = :idUser", nativeQuery = true)
    void unsubscribe(@Param("idDictionary") long idDictionary, @Param("idUser") long idUser);

    @Query(
            value = "select case when (count(*) = 1) then 'true' else 'false' end  FROM lexicon.dictionary_users Where id_dictionary = :idDictionary and id_user = :idUser",
            nativeQuery = true)
    boolean isUserSubscribedById(@Param("idDictionary") long idDictionary, @Param("idUser") long idUser);

}
