package org.happybaras.server.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedHouseDTO {
    private int totalPages;
    private int totalElements;
    private int page;
    private List<FindHouseDTO> houses;
}
