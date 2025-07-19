package org.hiring.api.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PagedResult<T> implements Iterable<T>{
    int page;
    int size;
    long totalCount;
    int totalPages;
    Collection<T> content;

    private PagedResult(final Collection<T> content, final int page, final int size, final long totalCount) {
        this.content = Collections.unmodifiableCollection(content);
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.totalPages = size > 0 ? (int) Math.ceil((double) totalCount / size) : 0;
    }

    public static <T> PagedResult<T> of(final Collection<T> content, final int page, final int size, final long totalCount) {
        return new PagedResult<>(content, page, size, totalCount);
    }

    public boolean hasNext() {
        return page < totalPages - 1;
    }

    public boolean hasPrevious() {
        return page > 0;
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}
