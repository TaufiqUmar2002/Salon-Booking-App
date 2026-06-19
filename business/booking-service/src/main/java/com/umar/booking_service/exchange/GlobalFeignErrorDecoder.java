package com.umar.booking_service.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.exceptions.common.exception.FeignErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class GlobalFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        if(response.status()==404 && response.body()!=null){
            try(InputStream bodyIs =response.body().asInputStream()){
                FeignErrorResponse errorResponse = mapper.readValue(bodyIs, FeignErrorResponse.class);
                return new ApiException(HttpStatus.NOT_FOUND,errorResponse.getCode(),errorResponse.getMessage());
            } catch (IOException e) {
                return new ApiException(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "Resource not found");
            }
        }
        if (response.status() == 403) {
            return new ApiException(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "You do not have permission to access this resource");
        }
        return new ErrorDecoder.Default().decode(methodKey, response);
    }
}
