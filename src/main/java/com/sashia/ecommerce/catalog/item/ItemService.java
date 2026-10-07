package com.sashia.ecommerce.catalog.item;

import com.sashia.ecommerce.catalog.item.dto.ItemSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemService {

    Page<ItemSummaryResponse> getAll(Pageable pageable);

    Optional<ItemSummaryResponse> get(Long id);

}
