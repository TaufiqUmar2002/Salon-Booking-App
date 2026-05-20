package salon_service.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter
public class OpeningHoursConverter
        implements AttributeConverter<Map<String, String>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {

        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert opening hours");
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {

        try {
            return mapper.readValue(
                    dbData,
                    new TypeReference<Map<String, String>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse opening hours");
        }
    }
}
