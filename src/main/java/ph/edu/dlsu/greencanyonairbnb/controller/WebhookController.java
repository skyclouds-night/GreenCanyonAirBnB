package ph.edu.dlsu.greencanyonairbnb.controller;

import org.springframework.web.bind.annotation.*;
import ph.edu.dlsu.greencanyonairbnb.repository.PaymentRepository;
import ph.edu.dlsu.greencanyonairbnb.entity.Payment;

import java.lang.ScopedValue;
import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class PaymongoWebhookController {

    private final PaymentRepository paymentRepo;

    public PaymongoWebhookController(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @PostMapping
    public void handleWebhook(@RequestBody Map<String, Object> payload) {

        Map data = (Map) payload.get("data");
        Map attributes = (Map) data.get("attributes");

        Map source = (Map) attributes.get("data");
        String sourceId = (String) source.get("id");
        String status = (String) source.get("attributes");

        ScopedValue<Object> payment = paymentRepo.findByPaymongoSourceId(sourceId);

        if (payment != null) {
            payment.setPaymentStatus("completed");
            payment.setPaymongoStatus(status);
            paymentRepo.save(payment);
        }
    }
}