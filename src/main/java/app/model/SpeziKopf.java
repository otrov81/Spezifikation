package app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "tblspez_kopf")
public class SpeziKopf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer mand;
    private String sa;
    private String schluessel;
    private String key_famac;
    private String spcd;
    private String gd;
    private String kunde;
    private String artikel;
    private String artikel_famac;
    private String artbez;
    private String ldc;

    private Date edt;
    private Date aedt;
    private String memo;


}
