package ViveSalud.demo.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DniService {

    private static final String API_URL = "https://api.decolecta.com/v1/reniec/dni?numero=";
    @Value("${apisnet.token}")
    private String TOKEN;

    public Map<String, Object> buscarPorDni(String dni) {
        RestTemplate restTemplate = new RestTemplate();

        // Headers requeridos por la API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Llamar a la API externa
        ResponseEntity<Map> response = restTemplate.exchange(
                API_URL + dni,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> data = response.getBody();
        System.out.println("🟢 Respuesta completa de la API DNI: " + data);

        if (data == null || data.isEmpty()) {
            return Map.of("error", "No se encontraron datos");
        }

        // 🔹 Obtener los valores de la API (asegurando que existan)
        String nombres = data.get("first_name") != null ? data.get("first_name").toString() : "";
        String apellidoPaterno = data.get("first_last_name") != null ? data.get("first_last_name").toString() : "";
        String apellidoMaterno = data.get("second_last_name") != null ? data.get("second_last_name").toString() : "";
        String apellidos = (apellidoPaterno + " " + apellidoMaterno).trim();

        // 🔹 Crear una respuesta personalizada
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("nombres", nombres);
        resultado.put("apellidos", apellidos);
        resultado.put("dni", data.get("document_number"));

        return resultado;
    }
}

