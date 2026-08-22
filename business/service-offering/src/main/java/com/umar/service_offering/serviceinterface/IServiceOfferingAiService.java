package com.umar.service_offering.serviceinterface;

import com.umar.payload.request.services.ai.AiBulkDescribeRequest;
import com.umar.payload.request.services.ai.AiServiceDescribe;
import com.umar.payload.response.services.ai.*;

public interface IServiceOfferingAiService {

    AiPriceSuggestResponse priceSuggest(Long serviceId);

    AiDescribeResponse describe(AiServiceDescribe aiServiceDescribe);

    AiServiceUpsellResponse upsell(Long serviceId);

    AiBulDescribeResponse bulkDescribe(Long salonId, AiBulkDescribeRequest aiBulkDescribeRequest);

    AiServicePerformance performance(Long salonId);
}
