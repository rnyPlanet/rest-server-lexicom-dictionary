package com.grin.lexicom.repository;

import com.grin.lexicom.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findAllByDictionaryId(Long id);

    @Modifying
    @Query("delete from Word w where w.id = ?1")
    void deleteById(Long entityId);
}
