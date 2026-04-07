package ph.edu.dlsu.greencanyonairbnb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.service.PropertyService;

import java.util.List;

@Controller
public class ListingViewController {
    @Autowired
    private PropertyService propertyService;

    @GetMapping("/all-properties")
    public String viewProperties(Model model) {
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties);
        return "ListingView"; //ListingView.html
    }
}