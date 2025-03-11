package com.talentstream.dto;

import javax.validation.constraints.*;

public class ResumeLanguagesDto {

  

    @NotBlank(message = "Language name is required")
    private String languageName;

   

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}
