package org.team5.api.feature.discussion;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AllSearchStrategy implements ThreadSearchStrategy {
    public ThreadSearchType type() {
        return ThreadSearchType.ALL;
    }

    public Specification<Thread> toSpecification(String query) {
        return (root, q, cb) -> {
            if (query == null || query.isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + query.toLowerCase(Locale.ROOT) + "%";
            return cb.or(
                cb.like(cb.lower(root.get("title")), pattern),
                cb.like(cb.lower(root.get("content")), pattern)
            );
        };
    }
}
