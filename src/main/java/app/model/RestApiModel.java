package app.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "tblspez_restapi")
@Entity
public class RestApiModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mischung;
    private int zut1;
    private int zut2;
    private int zut3;
}
