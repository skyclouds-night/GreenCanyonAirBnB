package ph.edu.dlsu.greencanyonairbnb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ph.edu.dlsu.greencanyonairbnb.entity.Payment;
import ph.edu.dlsu.greencanyonairbnb.entity.PaymentAccount;
import ph.edu.dlsu.greencanyonairbnb.repository.PaymentAccountRepository;
import ph.edu.dlsu.greencanyonairbnb.repository.PaymentRepository;

import java.util.Base64;
import java.util.Map;

@Service
public class PayMongoService {

    @Autowired
    private PaymentAccountRepository accountRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    public String createQR(Long adminId, Long bookingId, double amount) {

        // 1. Get admin payment account
        PaymentAccount account = accountRepo
                .findByAdminIdAndIsActiveTrue(adminId)
                .orElseThrow(() -> new RuntimeException("No active payment account"));

        // 2. Encode secret key
        String auth = Base64.getEncoder()
                .encodeToString((account.getPaymongoSecretKey() + ":").getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + auth);

        String body = "{"
                + "\"data\": {"
                + "\"attributes\": {"
                + "\"amount\": " + (int)(amount * 100) + ","
                + "\"type\": \"qrph\","
                + "\"currency\": \"PHP\","
                + "\"redirect\": {"
                + "\"success\": \"http://localhost:8080/success\","
                + "\"failed\": \"http://localhost:8080/failed\""
                + "}"
                + "}"
                + "}"
                + "}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.paymongo.com/v1/sources",
                request,
                Map.class
        );

        // 4. Extract response
        Map data = (Map) response.getBody().get("data");
        String sourceId = (String) data.get("id");

        Map attributes = (Map) data.get("attributes");
        Map qr = (Map) attributes.get("qr_code");

        String qrUrl = (String) qr.get("external_url");

        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAccountId(account.getAccountId());
        payment.setAmount(amount);
        payment.setPaymentMethod("QRPh");
        payment.setPaymentStatus("pending");
        payment.setPaymongoSourceId(sourceId);

        paymentRepo.save(payment);

        return qrUrl;
    }
}