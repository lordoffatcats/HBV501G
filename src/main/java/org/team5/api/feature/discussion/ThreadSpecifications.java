package org.team5.api.feature.discussion;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ThreadSpecifications {
    static Specification<Thread> hasCourseId(UUID courseId) {
        return (root, query, cb) ->
            courseId == null
                ? cb.conjunction()
                : cb.equal(root.get("course").get("id"), courseId);
    }

    static Specification<Thread> hasAuthorId(UUID authorId) {
        return (root, query, cb) ->
                authorId == null
                        ? cb.conjunction()
                        : cb.equal(root.get("author").get("id"), authorId);
    }
}
