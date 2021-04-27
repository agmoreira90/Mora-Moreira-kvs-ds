package com.fury.demo.kvsds.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private  Long id;
    private String nombre;
    private String apellido;
    private String genero;
    private Date fechaNacimiento;
}
