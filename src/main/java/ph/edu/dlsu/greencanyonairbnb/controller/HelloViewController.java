package ph.edu.dlsu.greencanyonairbnb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ph.edu.dlsu.greencanyonairbnb.service.PropertyService;
import ph.edu.dlsu.greencanyonairbnb.model.Property;

import java.util.List;

@Controller
public class HelloViewController {
    @Autowired
    private PropertyService propertyService;

    @GetMapping("/")
    public String home(Model model) {
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties);
        return "index";//index.html
    }
}