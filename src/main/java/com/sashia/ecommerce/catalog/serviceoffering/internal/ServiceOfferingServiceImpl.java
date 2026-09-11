package com.sashia.ecommerce.catalog.serviceoffering.internal;

import com.sashia.ecommerce.catalog.item.internal.ServiceOfferingSearchRequest;
import com.sashia.ecommerce.catalog.serviceoffering.ServiceOfferingRepository;
import com.sashia.ecommerce.catalog.serviceoffering.ServiceOfferingService;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceOfferingCreateRequest;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceOfferingResponse;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    public ServiceOfferingServiceImpl(ServiceOfferingRepository serviceOfferingRepository) {
        this.serviceOfferingRepository = serviceOfferingRepository;
    }

    @Override
    @Transactional
    public Long create(ServiceOfferingCreateRequest service) {
//        return serviceOfferingRepository.create(service);
        return null;
    }

    @Override
    public ServiceOfferingResponse get(Long id) {
        return null;
//        return serviceOfferingRepository.findById(id).map(ServiceOfferingResponse::toDTO)
//                .orElseThrow(() -> new InvalidResourceException("service.not.found"));
    }

    @Override
    public Page<ServiceOfferingResponse> getAll(Pageable pageable, ServiceOfferingSearchRequest search) {
//        return serviceOfferingRepository.findAll(pageable, search).map(ServiceOfferingResponse::toDTO);
        return null;
    }

    @Override
    @Transactional
    public void edit(Long id, ServiceUpdateDTO service) {
//        if (!serviceOfferingRepository.exists(id))
//            throw new InvalidResourceException("service.not.found");
//
//        serviceOfferingRepository.update(id, service);
    }

    @Override
    public void delete(Long id) {
//        if (!serviceOfferingRepository.exists(id))
//            throw new InvalidResourceException("service.not.found");
//
//        serviceOfferingRepository.delete(id);
    }

}
