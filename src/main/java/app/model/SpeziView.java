package app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "tblspez_detail")
public class SpeziView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer mand;
    private String sa;
    private String schluessel;
    private String spcd;
    private String kunde;
    private String artikel;
    private Date edt;
    private Date aedt;
    private String KDNam;
    private String artbez;
    private String ldc;

}
