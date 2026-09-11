package com.sashia.ecommerce.catalog.item;

import com.sashia.ecommerce.catalog.item.dto.ItemSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemService {

    Page<ItemSummaryDTO> getAll(Pageable pageable);

    Optional<ItemSummaryDTO> get(Long id);

}
