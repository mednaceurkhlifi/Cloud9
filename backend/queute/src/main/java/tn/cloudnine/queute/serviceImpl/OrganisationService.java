package tn.cloudnine.queute.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.UserRoleDTO;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.Organization;
import tn.cloudnine.queute.model.ServiceAndFeedback.organization.OrganizationEntityCountsDTO;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.repository.serviceRepo.FeedbackRepository;
import tn.cloudnine.queute.repository.serviceRepo.OfficeRepository;
import tn.cloudnine.queute.repository.serviceRepo.OrganizationRepository;
import tn.cloudnine.queute.repository.user.UserRepository;
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
    private FeedbackRepository feedbackRepository;
    @Autowired
    private OfficeRepository officesRepository;
   @Autowired
   private UserRepository userRepository;


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


    @Transactional
    public void deleteOrganisation(Long id) {
        Organization organization = organisationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        // Cascade delete feedbacks, offices, and users
        // The @OneToMany cascade REMOVE annotation should handle this, but we can manually delete them to ensure data integrity

        if (organization.getFeedbacks() != null && !organization.getFeedbacks().isEmpty()) {
            feedbackRepository.deleteAll(organization.getFeedbacks());
        }

        if (organization.getOffices() != null && !organization.getOffices().isEmpty()) {
            officesRepository.deleteAll(organization.getOffices());
        }

        if (organization.getUsers() != null && !organization.getUsers().isEmpty()) {
            userRepository.deleteAll(organization.getUsers());
        }

        // Now delete the organization itself
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
            officesCount.put(org.getOrganizationId(), org.getOffices().size());
        }

        return officesCount;
    }

    public List<OrganizationEntityCountsDTO> getAllOrganizationsEntityCounts() {
        List<Organization> organizations = organisationRepository.findAll();

        return organizations.stream().map(organization -> {
            int officeCount = organization.getOffices() != null ? organization.getOffices().size() : 0;
            int feedbackCount = organization.getFeedbacks() != null ? organization.getFeedbacks().size() : 0;
            int userCount = organization.getUsers() != null ? organization.getUsers().size() : 0;

            return new OrganizationEntityCountsDTO(
                    organization.getOrganizationId(),
                    organization.getName(),
                    officeCount,
                    feedbackCount,
                    userCount
            );
        }).toList();
    }




}