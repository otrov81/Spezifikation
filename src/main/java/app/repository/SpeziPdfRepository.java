package app.repository;

import app.model.SpeziPdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpeziPdfRepository extends JpaRepository<SpeziPdf, Integer> {

    //search be icon ->searchByIcon
    @Query(value = "SELECT * FROM `Spezi`.`tblspez_pdf` WHERE `logo`= ?1",nativeQuery = true)
    List<SpeziPdf> searchByIcon(String logo);

}
