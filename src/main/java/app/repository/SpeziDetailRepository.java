package app.repository;

import app.model.SpeziDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeziDetailRepository extends JpaRepository<SpeziDetail, Integer> {

    @Transactional
    @Modifying
    @Query(value = "SELECT tblspez_detail.id, tblspez_detail.spcd, tblspez_detail.schluessel, tblspez_detail.znr, " +
            "tblspez_detail.textart, tblspez_detail.key, " +
            "tblspez_detail.titel, tblspez_detail.text, tblspez_detail.edt, " +
            "tblspez_detail.aedt, tblspez_detail.sa, tblspez_detail.mand FROM tblspez_kopf " +
            "INNER JOIN tblspez_detail ON (tblspez_kopf.spcd = tblspez_detail.spcd) " +
            "AND (tblspez_kopf.schluessel = tblspez_detail.schluessel) AND (tblspez_kopf.sa = ?1) " +
            "AND (tblspez_kopf.mand = tblspez_detail.mand) " +
            "WHERE (((tblspez_detail.schluessel)=?2) AND ((tblspez_detail.spcd)=?3) " +
            "AND ((tblspez_detail.sa)=tblspez_kopf.sa)) ORDER BY tblspez_detail.znr;", nativeQuery = true)
    List<SpeziDetail> selectSpeziDetail(String sa, String schluessel, String spcd);
    @Transactional
    @Modifying
    @Query(value = "UPDATE `Spezi`.`tblspez_detail` SET `text`=?1, `key`=?2 WHERE  `mand`=?3 AND `sa`=?4 AND `schluessel`=?5 AND `spcd`=?6 AND `znr`=?7 AND `textart`=?8", nativeQuery = true)
    void updateSpeziDetail(String text, String key, Integer mand, String sa, String schluessel, String spcd, String znr, String textart);


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `Spezi`.`tblspez_detail` (`text`,`mand`,`sa`,`schluessel`,`spcd`,`znr`,`textart`,`titel`) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery = true)
    void insertSpeziDetail(String text, Integer mand, String sa, String schluessel, String spcd, String znr, String textart, String titel);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM `Spezi`.`tblspez_detail` WHERE  `mand`=?1 AND `sa`=?2 AND `schluessel`=?3 AND `spcd`=?4 AND `znr`=?5", nativeQuery = true)
    void deleteSpeziDetail(Integer mand, String sa, String schluessel, String spcd, String znr);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `Spezi`.`tblspez_detail` SET `znr`=?1 WHERE  `mand`=?2 AND `sa`=?3 AND `schluessel`=?4 AND `spcd`=?5 AND `textart`=?6", nativeQuery = true)
    void updateSpeziDetailTableSort(String znr, Integer mand, String sa, String schluessel, String spcd, String textart);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM `Spezi`.`tblspez_detail` WHERE  `mand`=?1 AND `sa`=?2 AND `schluessel`=?3 AND `spcd`=?4", nativeQuery = true)
    void deleteSpeziDetailComplet(Integer mand, String sa, String schluessel, String spcd);

    @Query(value = "SELECT COUNT(*) FROM `Spezi`.`tblspez_detail` WHERE schluessel=?1 AND spcd=?2 AND sa='03' AND mand='1000'", nativeQuery = true)
    Integer checkKunenNummer(String schluessel, String spcd);


    @Transactional
    @Modifying
    @Query(value = "UPDATE `Spezi`.`tblspez_detail` SET `titel`=?1 WHERE  `mand`=?2 AND `spcd`=?3 AND `znr`=?4", nativeQuery = true)
    void updateSpeziDetailTableTitel(String titel, Integer mand, String spcd, String znr);


}
