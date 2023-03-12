package com.github.vladbahlai.garage.service;

import com.github.vladbahlai.garage.model.Model;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ModelService {

    @Transactional(readOnly = true)
    List<Model> getModels();

    @Transactional(readOnly = true)
    Model getModelById(Long id);

    @Transactional(readOnly = true)
    Model getModelByName(String name);

    @Transactional
    void deleteModel(Long id);

    @Transactional
    Model saveModel(Model model);

    @Transactional(readOnly = true)
    List<Model> getModels(Integer pageNumber, Integer pageSize, String sortBy);

}
