package com.umar.service_offering.service;

import com.umar.payload.request.services.ai.AiBulkDescribeRequest;
import com.umar.payload.request.services.ai.AiServiceDescribe;
import com.umar.payload.response.services.ai.*;
import com.umar.service_offering.serviceinterface.IServiceOfferingAiService;
import org.springframework.stereotype.Service;

@Service
public class ServiceOfferingAiService implements IServiceOfferingAiService {


    @Override
    public AiPriceSuggestResponse priceSuggest(Long serviceId) {
        return null;
    }

    @Override
    public AiDescribeResponse describe(AiServiceDescribe aiServiceDescribe) {
        return null;
    }

    @Override
    public AiServiceUpsellResponse upsell(Long serviceId) {
        return null;
    }

    @Override
    public AiBulDescribeResponse bulkDescribe(Long salonId, AiBulkDescribeRequest aiBulkDescribeRequest) {
        return null;
    }

    @Override
    public AiServicePerformance performance(Long salonId) {
        return null;
    }
}
