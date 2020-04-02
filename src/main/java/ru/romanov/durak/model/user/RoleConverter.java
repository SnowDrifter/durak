package ru.romanov.durak.model.user;

import org.jooq.Converter;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class RoleConverter implements Converter<String, Set<Role>> {

    @Override
    public Set<Role> from(String rawData) {
        if (StringUtils.isEmpty(rawData)) {
            return new HashSet<>();
        }

        String[] roleNames = rawData.split(",");
        return Arrays.stream(roleNames)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    @Override
    public String to(Set<Role> roles) {
        List<String> roleNames = roles.stream()
                .map(Role::name)
                .collect(Collectors.toList());

        return String.join(",", roleNames);
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Set<Role>> toType() {
        return (Class<Set<Role>>) (Class<?>) Set.class;
    }
}
