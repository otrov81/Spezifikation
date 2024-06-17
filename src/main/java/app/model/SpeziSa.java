package app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tblspez_sa")
public class SpeziSa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer mand;
    private String sa;
    private String text_sa;

public SpeziSa(){
    //defaullt construktor
}
    public SpeziSa(String textSa) {
        this.text_sa = text_sa;
    }
}
