package Xeva.productiveApp.appUser;

import Xeva.productiveApp.graphicBackground.GraphicBackground;
import Xeva.productiveApp.locale.Locale;
import Xeva.productiveApp.userRelation.UserRelation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class ApplicationUser implements UserDetails {

    /*
    Dane użytkownika:
    - Id Long klucz główny
    - firstName String
    - lastName String
    - email String
    - password String
    - userRole enum
    - locked boolean - blokada użytkownika
    - enabled boolean - czy konto zostało aktywowane?
    - listOfCollaborators - lista znajomych
    */
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    private String firstName;
    private String lastName;
    @Column(
        nullable=false
    )
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private AppUserRole userRole;

    @JsonIgnore
    private Boolean locked = false;

    @JsonIgnore
    private Boolean enabled = true;

    @OneToMany(mappedBy = "user2")
    @JsonIgnore
    private List<UserRelation> listOfCollaborators;

    @ManyToOne
    @JoinColumn(name = "locale_id")
    @JsonIgnore
    private Locale locale;

    @ManyToOne
    @JoinColumn(name = "graphic_background_id")
    @JsonIgnore
    private GraphicBackground graphicBackground;

    private LocalDateTime lastUpdatedLocale = LocalDateTime.now();
    private LocalDateTime lastUpdatedGraphic = LocalDateTime.now();

    public ApplicationUser(String firstName, String lastName, String email, String password, AppUserRole userRole) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;

    }

    @Override
    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.userRole.name());

        return Collections.singletonList(authority);
    }

    @Override
    public java.lang.String getPassword() {
        return this.password;
    }

    @Override
    public java.lang.String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public ApplicationUser(String firstName, String lastName, String email, AppUserRole userRole){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userRole = userRole;
    }

}
