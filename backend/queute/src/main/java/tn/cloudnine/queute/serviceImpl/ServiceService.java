package tn.cloudnine.queute.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.ServiceAndFeedback.office.Office;
import tn.cloudnine.queute.model.ServiceAndFeedback.services.Services;
import tn.cloudnine.queute.repository.serviceRepo.OfficeRepository;
import tn.cloudnine.queute.repository.serviceRepo.ServiceRepository;
import tn.cloudnine.queute.services.IServiceService;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService implements IServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private OfficeRepository officeRepository;

    @Override
    public List<Services> findAll() {
        return serviceRepository.findAll ();
    }

    @Override
    public Optional<Services> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public Services createServiceWithOfficeId(Services service, Long officeId) {

            Office office = officeRepository.findById(officeId)
                    .orElseThrow(() -> new RuntimeException("Office not found"));

            service.setOffice(office);
            return serviceRepository.save(service);
        }




    public Services createService(Services service) {
        Long officeId = service.getOffice().getOfficeId();
        Office office = officeRepository.findById(officeId)
                .orElseThrow(() -> new RuntimeException("Office not found"));

        service.setOffice(office);
        return serviceRepository.save(service);
    }


    @Override
    public void deleteById(Long id) {
        serviceRepository.deleteById(id);
    }

    public Services updateService(Long id, Services serviceDetails) {
        Services existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));

        if (serviceDetails.getServiceName() != null) {
            existingService.setServiceName(serviceDetails.getServiceName());
        }
        if (serviceDetails.getDescription() != null) {
            existingService.setDescription(serviceDetails.getDescription());
        }
        if (serviceDetails.getType() != null) {
            existingService.setType(serviceDetails.getType());
        }

        return serviceRepository.save(existingService);
    }


}
