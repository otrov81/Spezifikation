package app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tblspez_memo")
public class SpeziMemo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer mand;
    private String sa;
    private String schluessel;
    private String spcd;
    private String memo;
}