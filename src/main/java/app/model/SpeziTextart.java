package app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tbltpez_textart")
public class SpeziTextart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer mand;
    private String spcd;
    private String textart;
    private String key_neu;
    private String element;
    private String znr;
    private String titel;
    private String such;
    private String uebenehmen;
    private Integer textblock_kunde;
    private Integer textblock_artikel;
    private Integer textblock_kab;

    public  SpeziTextart(){}

    public SpeziTextart(String textart, String znr, String titel) {
        this.textart = textart;
        this.znr = znr;
        this.titel = titel;
    }
}
