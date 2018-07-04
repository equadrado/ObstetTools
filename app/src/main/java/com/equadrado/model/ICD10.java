package com.equadrado.model;

/**
 * Created by equadrado on 2016-11-10.
 */

public class ICD10 {
    private String code;
    private String description;

    public ICD10(String code, String description){
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }
}
