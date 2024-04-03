package com.certificate.hub.domain.dto.request;

import com.certificate.hub.persistence.entity.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpDto {

    @Size(min = 4, max = 22)
    @NotEmpty(message = "Nombre de usuario requerido")
    private String username;
    @Email
    @NotEmpty(message = "Correo requerido")
    private String email;
    @Size(min = 4 , max = 10)
    @NotEmpty(message = "Nombre requerido")
    private String firstName;
    @Size(min = 4, max = 10)
    @NotEmpty(message = "Apellido requerido")
    private String lastName;
    @Size(min = 4, max = 22)
    @NotEmpty(message = "Contraseña requerida")
    private String password;
    @NotEmpty(message = "Rol requerido")
    @Pattern(regexp = "ADMIN|OPERATOR", message = "Rol inválido")
    private String rol;

}
