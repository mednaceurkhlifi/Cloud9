package tn.cloudnine.queute.service.roadmap;

import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.roadmap.StepProgress;

import java.util.Optional;

@Service
public interface StepProgressService {
    public void update(Long id, StepProgress stepStepProgress) ;
    public Optional<StepProgress> findById(Long id);
    public boolean updateStatus(Long id);
}
