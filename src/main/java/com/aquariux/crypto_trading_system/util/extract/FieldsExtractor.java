package com.aquariux.crypto_trading_system.util.extract;

import com.aquariux.crypto_trading_system.util.validation.Sortable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Component
public class FieldsExtractor {

    private final Map<Class<?>, Set<String>> clazzToFields = new ConcurrentHashMap<>();

    public boolean checkValidSortableField(String sortString, Class<?> clazz) {
        String lowerSortString = sortString.toLowerCase(Locale.ROOT);

        Set<String> fieldNames = clazzToFields.computeIfAbsent(clazz, this::extractSortableFieldNames);

        return fieldNames.contains(lowerSortString);
    }

    private Set<String> extractSortableFieldNames(Class<?> clazz) {

        Set<String> fieldNames = new HashSet<>();

        Stream.concat(Arrays.stream(clazz.getSuperclass().getDeclaredFields()),
                        Arrays.stream(clazz.getDeclaredFields()))
                .filter(field -> field.getAnnotationsByType(Sortable.class).length != 0)
                .map(Field::getName)
                .map(fieldName -> fieldName.toLowerCase(Locale.ROOT))
                .forEach(fieldNames::add);

        return fieldNames;
    }
}
