package io.v47.tmdb.http.utils;

import io.vertx.mutiny.core.MultiMap;

import java.util.*;

public final class MultiMapUtil {
    private MultiMapUtil() {
    }

    public static Map<String, List<String>> convertToMap(MultiMap multiMap) {
        HashMap<String, List<String>> result = new HashMap<>();
        for (Map.Entry<String, String> entry : multiMap) {
            result.merge(entry.getKey(), Collections.singletonList(entry.getValue()), (existing, more) -> {
                if (existing instanceof ArrayList<?>) {
                    existing.addAll(more);
                    return existing;
                } else {
                    ArrayList<String> innerResult = new ArrayList<>(existing);
                    innerResult.addAll(more);

                    return innerResult;
                }
            });
        }

        return result;
    }
}
