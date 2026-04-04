package ph.edu.dlsu.greencanyonairbnb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/") // Maps to http://localhost:8080/
    public String home(Model model) {
        // This 'message' is passed to the HTML th:text
        model.addAttribute("message", "System is Online & Ready!");
        return "index"; // Looks for templates/index.html
    }
}