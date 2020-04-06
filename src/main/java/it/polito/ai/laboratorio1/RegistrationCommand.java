package it.polito.ai.laboratorio1;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class RegistrationCommand {
    @Size(min=2, max=50, message="Name size must be between 2 and 50 characters")
    public String registrationName;
    @Size(min=2, max=50, message="Surname size must be between 2 and 50 characters")
    public String registrationSurname;
    @Email(message = "Email  format required")
    public String registrationEmail;
    @Size(min=6, max=50, message="Password size must be between 6 and 50 characters")
    public String registrationPassword;
    @Size(min=6, max=50, message="Password size must be between 6 and 50 characters")
    public String registrationPassword2;
    public Boolean registrationConditions;
}
