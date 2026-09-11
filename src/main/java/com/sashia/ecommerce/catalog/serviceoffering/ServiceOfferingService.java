package com.sashia.ecommerce.catalog.serviceoffering;

import com.sashia.ecommerce.catalog.item.internal.ServiceOfferingSearchRequest;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceOfferingCreateRequest;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceOfferingResponse;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceOfferingService {

    Long create(ServiceOfferingCreateRequest service);

    ServiceOfferingResponse get(Long id);

    Page<ServiceOfferingResponse> getAll(Pageable pageable, ServiceOfferingSearchRequest filter);

    void edit(Long id, ServiceUpdateDTO service);

    void delete(Long id);

}