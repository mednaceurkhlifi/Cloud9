package tn.cloudnine.queute.controllers.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.ServiceAndFeedback.office.Office;
import tn.cloudnine.queute.serviceImpl.OfficeService;


import java.util.List;


@RestController
@RequestMapping("/offices")
@CrossOrigin(origins = "http://localhost:4200")
public class OfficeController {

    @Autowired
    private OfficeService officeService;



    @GetMapping("/by-organisation/{organisationId}")
    public ResponseEntity<List<Office>> getAllOfficesByOrganisationId(
            @PathVariable Long organisationId) {
        List<Office> offices = officeService.getAllOfficesByOrganisationId(organisationId);
        return ResponseEntity.ok(offices);
    }

    @GetMapping("/{id}")
    @JsonIgnore
    public Office getOfficeById(@PathVariable Long id) {
        return officeService.getOfficeById(id);
    }





    @PatchMapping("/updateOffice/{id}")
    public Office updateOffice(@PathVariable Long id, @RequestBody Office office) {
        return officeService.updateOffice(id, office);
    }

    @DeleteMapping("/deleteOffice/{id}")
    public void deleteOffice(@PathVariable Long id) {
        officeService.deleteOffice(id);
    }

    @PostMapping("/assign-to-organisation/{organisationId}")
    public ResponseEntity<Office> addOfficeAndAssignToOrganisation(
            @PathVariable Long organisationId,
            @Valid @RequestBody Office office) {
        Office savedOffice = officeService.addOfficeAndAssignToOrganisation(organisationId, office);
        return ResponseEntity.ok(savedOffice);
    }
}
