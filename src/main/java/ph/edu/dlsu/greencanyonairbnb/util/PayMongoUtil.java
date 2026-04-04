package ph.edu.dlsu.greencanyonairbnb.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class PayMongoUtil {

    public HttpHeaders createHeaders(String secretKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String auth = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes());

        headers.set("Authorization", "Basic " + auth);
        return headers;
    }
}