package com.github.vladbahlai.garage.service.impl;

import com.github.vladbahlai.garage.exception.ModelNotFoundException;
import com.github.vladbahlai.garage.exception.UniqueNameConstraintException;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.repository.ModelRepository;
import com.github.vladbahlai.garage.service.ModelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository repository;

    public ModelServiceImpl(ModelRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Model> getModels() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Model getModelById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ModelNotFoundException(id));
    }

    @Override
    public Model getModelByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new ModelNotFoundException(name));
    }

    @Override
    @Transactional
    public void deleteModel(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Model saveModel(Model model) {
        if ((repository.existsByName(model.getName()) && model.getId() == null) ||
                (model.getId() != null && repository.existsByName(model.getName()) &&
                        !model.getName().equals(repository.findById(model.getId()).orElseThrow(() -> new ModelNotFoundException(model.getId())).getName()))) {
            throw new UniqueNameConstraintException("Model with " + model.getName() + " name already exist.");
        }
        return repository.save(model);
    }

    @Override
    public List<Model> getModels(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Model> page = repository.findAll(paging);

        if (page.hasContent()) {
            return page.getContent();
        }
        return new ArrayList<>();
    }
}
