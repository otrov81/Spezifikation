package app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tblspez_pdf")
public class SpeziPdf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String adresskopf1;
    private String adresskopf2;
    private String adressdetail1;
    private String adressdetail2;
    private String bankkopf1;
    private String bankkopf2;
    private String bankkopf3;
    private String bankkopf4;
    private String bankdetail1;
    private String bankdetail2;
    private String bankdetail3;
    private String bankdetail4;
    private String logo;
    private String detailinfo;
}
