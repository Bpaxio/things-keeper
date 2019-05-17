package ru.bbpax.keeper.security.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {
    @Id
    @Field("_id")
    private String id;
    private String password;
    private String username;
    private Set<GrantedAuthority> authorities;
    private Set<Privilege> privileges;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public User() {
        this(new ObjectId().toHexString());
    }

    public User(String id) {
        this.id = id;
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.authorities = Collections.emptySet();
        this.privileges = Collections.emptySet();
    }
}
