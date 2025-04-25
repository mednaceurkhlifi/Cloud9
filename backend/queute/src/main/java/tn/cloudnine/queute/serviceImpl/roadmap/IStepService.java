package tn.cloudnine.queute.serviceImpl.roadmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.roadmap.Step;
import tn.cloudnine.queute.repository.roadmap.StepRepository;
import tn.cloudnine.queute.service.roadmap.StepService;

import java.util.List;
import java.util.Optional;

@Service
public class IStepService implements StepService {

    public final StepRepository stepRepository;
    @Autowired
    public IStepService(StepRepository stepRepository){
        this.stepRepository=stepRepository;
    }

    @Override
    public void add(Step step) {
        this.stepRepository.save(step);
    }

    @Override
    public void delete(Long id) {
        this.stepRepository.deleteById(id);

    }

    @Override
    public void update(Long id, Step step) {
        step.setId(id);
        this.stepRepository.save(step);

    }

    @Override
    public Optional<Step> findById(Long id) {
        return this.stepRepository.findById(id);
    }

    @Override
    public List<Step> getAll() {
        return this.stepRepository.findAll();
    }

}
