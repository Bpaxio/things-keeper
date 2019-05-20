package ru.bbpax.keeper.security.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String, List<Privilege>> privileges;

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
        this.privileges = new HashMap<>();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(this.enabled).append("; ");
        sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
        sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired)
                .append("; ");
        sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

        if (!authorities.isEmpty()) {
            sb.append("Granted Authorities: ");

            boolean first = true;
            for (GrantedAuthority auth : authorities) {
                if (!first) {
                    sb.append(",");
                }
                first = false;

                sb.append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }
        if (privileges != null && !privileges.isEmpty()) {
            sb.append("Granted Privileges: ");
            privileges.forEach((key, value) -> sb.append(key).append(" - ").append(value));

        } else {
            sb.append("Not granted any privileges");
        }

        return sb.toString();
    }
}
