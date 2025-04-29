package tn.cloudnine.queute.controllers.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import tn.cloudnine.queute.config.user.UserToUserDetails;
import tn.cloudnine.queute.dto.UserRoleDTO;
import tn.cloudnine.queute.dto.requests.OrganizationCreationRequest;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.Organization;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.OrganizationEntityCountsDTO;
import tn.cloudnine.queute.service.user.JwtService;
import tn.cloudnine.queute.service.user.UserServiceImpl;
import tn.cloudnine.queute.serviceImpl.FeedbackService;
import tn.cloudnine.queute.serviceImpl.OrganisationService;
import org.springframework.core.io.Resource;
import java.util.List;
import java.util.Map;

import tn.cloudnine.queute.utils.IFileUploader;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/organisations")
public class OrganisationController {
    @Autowired
    private IFileUploader fileUploader;

    @Autowired
    private OrganisationService organisationService;
    @Autowired
    private FeedbackService feedbackService;

    // Récupérer toutes les organisations
    @GetMapping
    public List<Organization> getAllOrganisations() {
        return organisationService.findAll();
    }


    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        return fileUploader.serveFile("images", filename, "image/jpeg");
    }


    @PostMapping(value = "/{user_email}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Organization> createOrganisation(
            @ModelAttribute Organization requestDTO,
            @PathVariable("user_email") String user_email,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            Organization saved = organisationService.createOrganization(requestDTO, imageFile, user_email);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Organization> updateOrganisation(
            @PathVariable Long id,
            @ModelAttribute Organization organizationDetails,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            Organization updatedOrg = organisationService.updateOrganisation(id, organizationDetails, imageFile);
            return ResponseEntity.ok(updatedOrg);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganisationById(@PathVariable Long id) {
        return organisationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("IsDeleted/{id}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable Long id) {
        if (organisationService.existsById(id)) {
            organisationService.deleteOrganisation(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{organizationId}/average-rate")
    public Double getAverageRate(@PathVariable Long organizationId) {
        return feedbackService.getAverageRateByOrganizationId(organizationId);
    }

    @GetMapping("/global")
    public Map<String, Object> getGlobalStatistics() {
        return organisationService.getGlobalStatistics();
    }

    @GetMapping("/offices-count")
    public Map<Long, Integer> getOfficesCountByOrganization() {
        return organisationService.getOfficesCountByOrganization();
    }


    @GetMapping("/counts")
    public ResponseEntity<List<OrganizationEntityCountsDTO>> getAllOrganizationsEntityCounts() {
        List<OrganizationEntityCountsDTO> counts = organisationService.getAllOrganizationsEntityCounts();
        return ResponseEntity.ok(counts);
    }


}