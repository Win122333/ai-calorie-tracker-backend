package ru.vsu.cs.fitAssistant.profile.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Достаем стандартные скоупы (openid, email и т.д.)
        var defaultConverter = new JwtGrantedAuthoritiesConverter();
        Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);

        // Достаем роли из realm_access.roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return authorities;
        }

        Collection<String> roles = (Collection<String>) realmAccess.get("roles");

        // Превращаем их в ROLE_NAME и объединяем со стандартными
        return Stream.concat(
                authorities.stream(),
                roles.stream()
                        .map(roleName -> "ROLE_" + roleName) // Важно: добавляем префикс!
                        .map(SimpleGrantedAuthority::new)
        ).collect(Collectors.toList());
    }
}
