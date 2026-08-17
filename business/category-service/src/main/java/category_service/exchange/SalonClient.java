package category_service.exchange;


import com.umar.payload.response.salon.SalonResponseList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SALON-SERVICE",url = "http://localhost:5009")
public interface SalonClient {

    @GetMapping("api//salon/category/{categoryId}")
    SalonResponseList getSalonByCategory(@PathVariable("categoryId") Long id);
}
