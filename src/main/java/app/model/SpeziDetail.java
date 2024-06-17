package app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "tblspez_detail")
public class SpeziDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer mand;
    private String sa;
    private String schluessel;
    //private String key_famac;
    private String spcd;
    private String znr;
    private String textart;
    //private String element_abas;
    //private String textart_abas;
    private String key;
    //private String key_abas;
    private String titel;
    private String text;
    private Date edt;
    private Date aedt;
}
