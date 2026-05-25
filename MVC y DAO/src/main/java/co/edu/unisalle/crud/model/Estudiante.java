package co.edu.unisalle.crud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Modelo del patrón MVC.
 * SRP: representa únicamente los datos de un estudiante; no conoce nada sobre
 * persistencia, vista ni reglas de orquestación.
 */
public class Estudiante {

    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String programa;

    public Estudiante() {
    }

    @JsonCreator
    public Estudiante(@JsonProperty("id") int id,
                      @JsonProperty("nombre") String nombre,
                      @JsonProperty("apellido") String apellido,
                      @JsonProperty("correo") String correo,
                      @JsonProperty("programa") String programa) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.programa = programa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estudiante that)) return false;
        return id == that.id
                && Objects.equals(nombre, that.nombre)
                && Objects.equals(apellido, that.apellido)
                && Objects.equals(correo, that.correo)
                && Objects.equals(programa, that.programa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, correo, programa);
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", programa='" + programa + '\'' +
                '}';
    }
}
