package com.src.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class UserInformationDTO {
    private String name;

    private Date birthDate;
}
