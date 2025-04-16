package tn.cloudnine.queute.controllers.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.Organization;
import tn.cloudnine.queute.repository.serviceRepo.OrganizationRepository;
import tn.cloudnine.queute.serviceImpl.OrganisationService;
import org.springframework.core.io.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import tn.cloudnine.queute.utils.IFileUploader;
import org.springframework.web.multipart.MultipartFile;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/organisations")
public class OrganisationController {
    @Autowired
    private IFileUploader fileUploader;

    @Autowired
    private OrganisationService organisationService;

    // Récupérer toutes les organisations
    @GetMapping
    public List<Organization> getAllOrganisations() {
        return organisationService.findAll();
    }


    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        return fileUploader.serveFile("images", filename, "image/jpeg");
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Organization> createOrganisation(
            @ModelAttribute Organization requestDTO,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            Organization saved = organisationService.createOrganization(requestDTO, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Récupérer une organisation par ID



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

    // Supprimer une organisation par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable Long id) {
        if (organisationService.existsById(id)) {  // Vérifiez si l'organisation existe
            organisationService.deleteOrganisation(id);
            return ResponseEntity.noContent().build();  // 204 No Content
        } else {
            return ResponseEntity.notFound().build();  // 404 Not Found
        }
    }

}