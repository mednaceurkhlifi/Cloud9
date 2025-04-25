package tn.cloudnine.queute.service.roadmap;

import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.roadmap.Step;

import java.util.List;
import java.util.Optional;
@Service
public interface StepService {
    public void add(Step step);
    public void delete(Long id);
    public void update(Long id,Step step) ;
    public Optional<Step> findById(Long id);
    public List<Step> getAll();
}
