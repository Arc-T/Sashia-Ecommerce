package com.sashia.ecommerce.catalog.item.internal;

import com.sashia.ecommerce.catalog.item.ItemMapper;
import com.sashia.ecommerce.catalog.item.ItemRepository;
import com.sashia.ecommerce.catalog.item.ItemService;
import com.sashia.ecommerce.catalog.item.dto.ItemSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Page<ItemSummaryDTO> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<ItemSummaryDTO> get(Long id) {
        return itemRepository.findById(id).map(ItemMapper::toDTO);
    }

}
