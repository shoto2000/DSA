package com.app.Helpers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;


public class PaginationHelper {
    public static <T> Page<T> getPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        if (start > end) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        } else {
            List<T> pageList = list.subList(start, end);
            return new PageImpl<>(pageList, pageable, list.size());
        }
    }
}
