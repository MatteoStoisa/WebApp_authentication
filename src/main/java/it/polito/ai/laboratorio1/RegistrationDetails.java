package it.polito.ai.laboratorio1;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class RegistrationDetails {
    public String userName;
    public String userSurmane;
    public String userEmail;
    public String userPassword;
    public Date userDate;

}
