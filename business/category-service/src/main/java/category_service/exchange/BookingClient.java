package category_service.exchange;

import com.umar.payload.response.booking.UserBookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BOOKING-SERVICE",url = "http://localhost:5009",configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface BookingClient {

    @GetMapping("/booking/{categoryId}")
    UserBookingResponse getBookingByCategory(@PathVariable("categoryId") Long id);

}
