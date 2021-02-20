package ru.otus.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Size(min=2, message = "Не меньше 5 знаков")
    private String username;

    private String email;

    @Size(min=2, message = "Не меньше 3 знаков")
    private String password;

    @Column(name = "passwordconfirm")
    private String passwordConfirm;

    private String name;

    private String lastname;

    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<PostRecord> records;

//    @ManyToMany(cascade = CascadeType.MERGE)
//    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Column(name = "accountnonexpired")
    private Boolean accountNonExpired;

    @Column(name = "accountnonlocked")
    private Boolean accountNonLocked;

    @Column(name = "credentialsnonexpired")
    private Boolean credentialsNonExpired;

    //for using two factor autentication
    private boolean isUsing2FA;
    private String secret;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Comment> comments;

    public User(String username) {
        this.username = username;
        this.secret = Base32.random();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.secret = Base32.random();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
