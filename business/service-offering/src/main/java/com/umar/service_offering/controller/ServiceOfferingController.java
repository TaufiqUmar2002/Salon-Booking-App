package com.umar.service_offering.controller;

import com.umar.payload.request.services.*;
import com.umar.payload.response.services.BulkUpdateServiceResponse;
import com.umar.payload.response.services.SearchServiceResponseList;
import com.umar.payload.response.services.ServiceResponse;
import com.umar.service_offering.serviceinterface.IServiceOfferingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final IServiceOfferingService service;

    @PreAuthorize("hasRole('SALON_OWNER')")
    @PostMapping
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody CreateServiceRequest request){
        ServiceResponse response = this.service.createServiceOffering(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALON_OWNER')")
    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Page<ServiceResponse>> getServiceBySalonId(@PathVariable Long salonId, @ModelAttribute ServiceSalonSearchRequest request){
        Page<ServiceResponse> serviceResponsePage = service.getAllServicesBySalonId(salonId,request);
        return ResponseEntity.status(HttpStatus.OK).body(serviceResponsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable("id") Long id){
        ServiceResponse serviceResponse = service.getAllServicesById(id);
        return ResponseEntity.ok(serviceResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updateService(@PathVariable Long id,@Valid@RequestBody UpdateServiceRequest request){
        ServiceResponse serviceResponse = service.updateService(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(serviceResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id,@Valid@RequestBody DeleteServiceRequest request){
        service.deleteService(id,request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulkUpdate")
    public ResponseEntity<BulkUpdateServiceResponse> serviceBulkUpdate(@Valid @RequestBody BulkUpdateServiceRequest bulkUpdateServiceRequest){
        BulkUpdateServiceResponse serviceResponse = this.service.bulkUpdateService(bulkUpdateServiceRequest);
        return ResponseEntity.status(HttpStatus.OK).body(serviceResponse);
    }

    @PostMapping("/{id}/clone")
    public ResponseEntity<ServiceResponse> cloneService(@PathVariable Long id,@RequestBody@Valid CloneServiceRequest request){
        ServiceResponse cloneServiceResponse =service.cloneService(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(cloneServiceResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchServiceResponseList> searchService(@ModelAttribute ServiceSearchRequest request){
        SearchServiceResponseList responseList = service.searchService(request);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

}
