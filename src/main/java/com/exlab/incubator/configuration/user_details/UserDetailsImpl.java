package com.exlab.incubator.configuration.user_details;

import com.exlab.incubator.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String phoneNumber;

    private Collection<? extends GrantedAuthority> authorities;


    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
            .stream().map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
        return new UserDetailsImpl(user.getId(), user.getUsername(),  user.getPassword(),
            user.getEmail(), user.getPhoneNumber(), authorities);
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
        return true;
    }
}