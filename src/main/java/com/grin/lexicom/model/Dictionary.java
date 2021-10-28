package com.grin.lexicom.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.grin.lexicom.util.validation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dictionary")
@Data
public class Dictionary extends BaseEntity {

    @JsonSerialize(using = DictionaryCreatorSerializer.class)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;

    @JoinColumn(name = "id_language", referencedColumnName = "id")
    @ManyToOne
    @ValidLang
    private Language lang;

    @NotNull
    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotNull
    @NotEmpty
    @Column(name = "description")
    private String description;

    @Column(name = "is_private")
    private boolean isPrivate = true;

    @Column(name = "url")
    private String url;

    @JsonIgnore
    @Column(name = "pass")
    private String pass;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dictionary")
    private List<Word> words;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonSerialize(using = UserSubscribeDictionarySerializer.class)
    @JoinTable(name = "dictionary_users", joinColumns = {
            @JoinColumn(name = "id_dictionary", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "id_user", referencedColumnName = "id")})
    @ManyToMany
    private Collection<User> usersCollection;


}
