package tn.cloudnine.queute.controllers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.ServiceAndFeedback.services.Services;
import tn.cloudnine.queute.serviceImpl.ServiceService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    // 🔹 GET all services
    @GetMapping("/all")
    public List<Services> getAllServices() {
        return serviceService.findAll();
    }

    // 🔹 GET service by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Services> getServiceById(@PathVariable Long id) {
        return serviceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 CREATE new service
    @PostMapping("/create/office/{officeId}")
    public ResponseEntity<?> createService(
            @PathVariable Long officeId,
            @RequestBody Services service) {

        try {
            Services createdService = serviceService.createServiceWithOfficeId(service, officeId);
            return ResponseEntity.ok(createdService);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }


    // 🔹 UPDATE existing service
    @PatchMapping("/update/{id}")
    public ResponseEntity<Services> updateService(@PathVariable Long id, @RequestBody Services serviceDetails) {
        Services updatedService = serviceService.updateService(id, serviceDetails);
        return ResponseEntity.ok(updatedService);
    }

    // 🔹 DELETE service by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        if (serviceService.findById(id).isPresent()) {
            serviceService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
