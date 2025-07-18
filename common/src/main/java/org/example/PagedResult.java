package org.example;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PagedResult<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalCount;
    private final int totalPages;

    private PagedResult(final List<T> content, final int page, final int size, final long totalCount) {
        this.content = Collections.unmodifiableList(content);
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.totalPages = size > 0 ? (int) Math.ceil((double) totalCount / size) : 0;
    }

    public static <T> PagedResult<T> of(final List<T> content, final int page, final int size, final long totalCount) {
        return new PagedResult<>(content, page, size, totalCount);
    }

    public boolean hasNext() {
        return page < totalPages - 1;
    }

    public boolean hasPrevious() {
        return page > 0;
    }
}