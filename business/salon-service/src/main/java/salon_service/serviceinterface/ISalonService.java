package salon_service.serviceinterface;



import com.umar.payload.request.salon.DeleteSalonRequest;
import com.umar.payload.request.salon.SalonRequest;
import com.umar.payload.request.salon.SalonSearchRequest;
import com.umar.payload.request.salon.UpdateSalonRequest;
import com.umar.payload.response.salon.SalonResponse;
import com.umar.payload.response.salon.SalonResponseList;
import com.umar.payload.response.salon.SalonResponseV1;
import salon_service.model.Salon;


public interface ISalonService {
    SalonResponse createSalon(SalonRequest salonRequest);
    SalonResponseList searchSalon(SalonSearchRequest request);
    SalonResponseV1 getSalonById(Long id);
    SalonResponseV1 updateSalon(UpdateSalonRequest request,Long salonId);
    void  deleteSalon(DeleteSalonRequest request,Long id);
    SalonResponseList getSalonByCategory(Long salonId);


}
