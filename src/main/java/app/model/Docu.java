package app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tblspez_docu")
public class Docu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String docu;
}
