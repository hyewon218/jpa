package me.hyewon.jpa.common;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
@AllArgsConstructor
public class PageDTO {

  //@Positive // 0보다 큰수
  private final Integer currentPage;
  private final Integer size;
  private String sortBy;

  public Pageable toPageable() {
    return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).descending());
  }
}
