package com.grin.lexicom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "language")
@Data
public class Language extends BaseEntity {

    @Column(name = "lang")
    private String lang;

    @Column(name = "lang_code")
    private String lang_code;

    @JsonIgnore
    @OneToMany(mappedBy = "lang")
    private List<Dictionary> dictionary;

    @Override
    public String toString() {
        return "Language { lang: " + lang + ", lang_code: " + lang_code + " }";
    }
}
