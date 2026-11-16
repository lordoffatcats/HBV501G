package org.team5.api.feature.discussion;

import org.springframework.data.jpa.domain.Specification;

public interface ThreadSearchStrategy {
    ThreadSearchType type(); // Gert svo að hægt sé að mappa type í viðeigandi strategíu.

    Specification<Thread> toSpecification(String query);
}
