package com.neoaplicacoes.customerapi.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtil {

  public static Pageable createPageable(int pageNumber, int size, String sort) {
    if (sort != null && !sort.trim().isEmpty()) {
      String[] sortParams = sort.split(",");
      if (sortParams.length == 2) {
        Sort.Direction direction =
            sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(pageNumber, size, Sort.by(direction, sortParams[0]));
      }
    }
    return PageRequest.of(pageNumber, size);
  }

  // Overloading for default values
  public static Pageable createPageable(int pageNumber, int size) {
    return createPageable(pageNumber, size, null);
  }

  // overload with pageNumber only (uses default size)
  public static Pageable createPageable(int pageNumber) {
    return createPageable(pageNumber, 10, null);
  }
}
