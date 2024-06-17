package app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tblspez_texte")
public class SpeziTexte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String mand;
    private String spcd;
    private String textart;
    private String such;
    private String key;
    private String KEY_2;
    private String text;

}
