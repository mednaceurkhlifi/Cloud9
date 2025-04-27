package tn.cloudnine.queute.serviceImpl.roadmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.roadmap.StepProgress;
import tn.cloudnine.queute.repository.roadmap.StepProgressRepository;
import tn.cloudnine.queute.service.roadmap.StepProgressService;

import java.util.Optional;

@Service

public class IStepProgressService  implements StepProgressService {
    private final StepProgressRepository stepProgressRepository;
    @Autowired
    public IStepProgressService(StepProgressRepository stepProgressRepository) {
        this.stepProgressRepository = stepProgressRepository;
    }

    @Override
    public void update(Long id, StepProgress step) {
        this.stepProgressRepository.save(step);
    }

    @Override
    public Optional<StepProgress> findById(Long id) {
        return this.stepProgressRepository.findById(id);
    }
    @Override
    public boolean updateStatus(Long id) {
        Optional<StepProgress> stepProgress_opt = this.stepProgressRepository.findById(id);
        if(stepProgress_opt.isPresent()){
            stepProgress_opt.get().setDone(!stepProgress_opt.get().isDone());
            return true ;
        }
        else {
            return false ;
        }
    }
}

