package salon_service.controller;



import com.umar.payload.request.salon.DeleteSalonRequest;
import com.umar.payload.request.salon.SalonRequest;
import com.umar.payload.request.salon.SalonSearchRequest;
import com.umar.payload.request.salon.UpdateSalonRequest;
import com.umar.payload.response.salon.SalonResponse;
import com.umar.payload.response.salon.SalonResponseList;
import com.umar.payload.response.salon.SalonResponseV1;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import salon_service.serviceinterface.ISalonService;

@Slf4j
@RestController
@RequestMapping("/api/salon")
@RequiredArgsConstructor
public class SalonController {

    private final ISalonService service;


    @PreAuthorize("hasRole('SALON_OWNER')")
    @PostMapping
    public ResponseEntity<SalonResponse> createSalon(@Valid @RequestBody SalonRequest request){
        SalonResponse response = service.createSalon(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<SalonResponseList> getSalons(@ModelAttribute SalonSearchRequest searchRequest){
        SalonResponseList responseList  =service.searchSalon(searchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalonResponseV1> getSalonById(@PathVariable("id") Long id){
        SalonResponseV1 responseV1 = service.getSalonById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseV1);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<SalonResponseList> getSalonByCategory(@PathVariable("categoryId") Long categoryId){
        SalonResponseList responseList = service.getSalonByCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALON_OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<SalonResponseV1>  updateSalon(@Valid@RequestBody UpdateSalonRequest request,@PathVariable("id") Long id){
        SalonResponseV1 salonResponseV1 = service.updateSalon(request,id);
        return ResponseEntity.status(HttpStatus.OK).body(salonResponseV1);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalon(@Valid @RequestBody DeleteSalonRequest request, @PathVariable("id") Long id){
         this.service.deleteSalon(request,id);
        return ResponseEntity.noContent().build();
    }


}
