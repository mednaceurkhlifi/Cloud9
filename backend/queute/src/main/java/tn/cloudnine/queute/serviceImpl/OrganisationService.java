package tn.cloudnine.queute.serviceImpl;

import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.Organization;
import tn.cloudnine.queute.repository.serviceRepo.OrganizationRepository;
import tn.cloudnine.queute.services.IOrganisationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.utils.FileUploader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



@Service
public class OrganisationService implements IOrganisationService {

    @Autowired
    private OrganizationRepository organisationRepository;



    @Autowired
    private FileUploader fileUploader;

    public List<Organization> findAll() {
        return organisationRepository.findAll();
    }

    public Optional<Organization> findById(Long id) {
        return organisationRepository.findById(id);
    }

    public Organization createOrganization(Organization requestDTO, MultipartFile imageFile) {
        // Construction de l'entité
        Organization organization = Organization.builder()
                .name(requestDTO.getName())
                .address(requestDTO.getAddress())
                .email(requestDTO.getEmail())
                .phone_number(requestDTO.getPhone_number())
                .is_deleted(false)
                .build();

        // Gestion du fichier image
        handleImageUpload(organization, imageFile);

        return organisationRepository.save(organization);
    }

    private void handleImageUpload(Organization organization, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imagePath = fileUploader.saveImage(imageFile);
                if (imagePath == null) {
                    throw new RuntimeException("Échec de la création du dossier de stockage");
                }
                if ("UNKNOWN".equals(imagePath)) {
                    throw new RuntimeException("Échec de l'upload du fichier");
                }
                organization.setImage(imagePath);
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors du traitement de l'image: " + e.getMessage());
            }
        }
    }

    public Organization updateOrganisation(Long id, Organization newData, MultipartFile imageFile) {
        return organisationRepository.findById(id).map(existingOrg -> {
            existingOrg.setName(newData.getName());
            existingOrg.setAddress(newData.getAddress());
            existingOrg.setEmail(newData.getEmail());
            existingOrg.setPhone_number(newData.getPhone_number());

            if (imageFile != null && !imageFile.isEmpty()) {
                // Utilisation de FileUploader pour sauvegarder l'image
                String imagePath = fileUploader.saveImage(imageFile);
                existingOrg.setImage(imagePath);
            }

            return organisationRepository.save(existingOrg);
        }).orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
    }


    public void deleteOrganisation(Long id) {
        organisationRepository.deleteById(id);
    }
    public boolean existsById(Long id) {
        return organisationRepository.existsById(id);
    }





   //Statics rate (avg)
    public Map<String, Object> getGlobalStatistics() {
        List<Organization> organizations = organisationRepository.findAll();

        double averageRate = organizations.stream()
                .mapToDouble(Organization::getAverageRate)
                .average()
                .orElse(0.0);

        int totalOffices = organizations.stream()
                .mapToInt(org -> org.getOffices().size())
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrganizations", organizations.size());
        stats.put("averageRate", averageRate);
        stats.put("totalOffices", totalOffices);

        return stats;
    }

    public Map<Long, Integer> getOfficesCountByOrganization() {
        List<Organization> organizations = organisationRepository.findAll();

        Map<Long, Integer> officesCount = new HashMap<>();
        for (Organization org : organizations) {
            officesCount.put(org.getId(), org.getOffices().size());
        }

        return officesCount;
    }
}