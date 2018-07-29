package com.larry.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Model <E>{

    private final Logger log = LoggerFactory.getLogger(Model.class);

    private String modelName;

    private List<E> elems;

    public Model(String name, List<E> elems) {
        this.modelName = name;
        this.elems = elems;
        log.info("model name : ", name);
        log.info("elems is : {}", elems);
    }

    public String getModelName() {
        return modelName;
    }

    public List<E> getElems() {
        return elems;
    }

    @Override
    public String toString() {
        return "Model{" +
                "modelName='" + modelName + '\'' +
                ", elems=" + elems +
                '}';
    }
}
