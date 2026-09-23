package com.mycomplex.pocauthorization.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class Auth0RolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    public static final String ROLES_CLAIM = "https://mycomplex.com/roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList(ROLES_CLAIM);
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (roles == null) {
            return authorities;
        }

        for (String role : roles) {
            authorities.add(
                new SimpleGrantedAuthority("ROLE_" + normalizeRole(role))
            );
        }

        return authorities;
    }

    private String normalizeRole(String role) {
        return role
            .trim()
            .toUpperCase()
            .replace("Á", "A")
            .replace("É", "E")
            .replace("Í", "I")
            .replace("Ó", "O")
            .replace("Ú", "U")
            .replace(" ", "_");
    }
}
