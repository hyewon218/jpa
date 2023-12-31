package me.hyewon.jpa.common;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PageDTO {

  //@Positive // 0보다 큰수
  private final Integer currentPage;
  private final Integer size;
  private String sortBy;

  public Pageable toPageable() {
    if (Objects.isNull(sortBy)) {
      return PageRequest.of(currentPage - 1, size); // null 일 경우 sortBy 하지 않도록(오류 방지)
    } else {
      return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).descending());
    }
  }

  public Pageable toPageable(String sortBy) {
    return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).descending());
  }
}
