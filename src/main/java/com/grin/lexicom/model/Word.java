package com.grin.lexicom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grin.domain.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "word")
@Data
public class Word extends BaseEntity {

    @JsonIgnore
    @JoinColumn(name = "id_dict", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dictionary dictionary;

    @NotNull
    @NotEmpty
    @Column(name = "native")
    private String nativeWord;

    @NotNull
    @NotEmpty
    @Column(name = "translate")
    private String translate;

}
