package ph.edu.dlsu.greencanyonairbnb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ph.edu.dlsu.greencanyonairbnb.service.PayMongoService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PayMongoService service;

    @PostMapping("/qr")
    public String generateQR(
            @RequestParam Long adminId,
            @RequestParam Long bookingId,
            @RequestParam double amount) {

        return service.createQR(adminId, bookingId, amount);
    }
}