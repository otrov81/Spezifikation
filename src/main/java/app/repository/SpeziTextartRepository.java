package app.repository;

import app.model.SpeziTextart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeziTextartRepository extends JpaRepository<SpeziTextart, Integer> {

    @Query(value ="SELECT * FROM tblspez_textart where tblspez_textart.spcd =?1 ORDER BY tblspez_textart.ZNR;", nativeQuery = true)
    List<SpeziTextart> selectTextart(String spcd);

    @Query(value ="SELECT * FROM tblspez_textart GROUP by spcd", nativeQuery = true)
    List<SpeziTextart> selectTextartSpcd();

    @Transactional
    @Modifying
    @Query(value = "UPDATE `Spezi`.`tblspez_textart` SET `titel`=?1 WHERE  `mand`=?2 AND `spcd`=?3 AND `znr`=?4 ", nativeQuery = true)
    void updateTextarteTitel(String titel, Integer mand, String spcd, String znr);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `Spezi`.`tblspez_textart` (`mand`, `spcd`, `textart`, `znr`, `titel`) VALUES (?1, ?2, ?3, ?4, ?5);", nativeQuery = true)
    void insetrTextartAll(Integer mand, String spcd, String textart, String znr, String titel);

    @Query(value = "SELECT COUNT(*) FROM tblspez_textart WHERE mand=?1 AND spcd =?2 AND `textart`=?3 AND `znr`=?4", nativeQuery = true)
    int checkIfZnrExistirt(Integer mand, String spcd, String textart, String znp);


}
