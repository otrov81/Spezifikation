package app.repository;

import app.model.SpeziKopf;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeziKopfRepository extends JpaRepository<SpeziKopf, Integer> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `Spezi`.`tblspez_kopf` (`mand`,`sa`,`schluessel`,`spcd`,`artikel`,`artbez`,`ldc`) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
    void insertSpeziKopfNeuFirst(Integer mand, String sa, String schluessel, String spcd, String artikel, String artbez, String ldc);

    @Query(value = "SELECT COUNT(*) FROM `Spezi`.`tblspez_kopf` WHERE `schluessel` = ?1 AND `spcd`= ?2 AND `mand`=?3 AND (IF(?4 = '', 1=1, `sa`=?4))", nativeQuery = true)
    int checkSpeziExistence(String schluessel, String spcd, Integer mand, String sa);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM `Spezi`.`tblspez_kopf` WHERE  `mand`=?1 AND `sa`=?2 AND `schluessel`=?3 AND `spcd`=?4", nativeQuery = true)
    void deleteSpeziKopfComplet(Integer mand, String sa, String schluessel, String spcd);

    @Query(value = "SELECT COUNT(*) FROM `Spezi`.`tblspez_kopf` WHERE `schluessel` = ?1 AND `spcd`= ?2 AND `mand`=?3 AND (IF(?4 = '', 1=1, `sa`=?4))", nativeQuery = true)
    int checkSpeziExistenceKunde(String schluessel, String spcd, Integer mand, String sa);
    @Query(value = "SELECT COUNT(*) FROM `Spezi`.`tblspez_kopf` WHERE `schluessel` = ?1 AND `spcd`= ?2 AND `mand`=?3 AND (IF(?4 = '', 1=1, `sa`=?4))", nativeQuery = true)
    int checkSpeziExistenceLand(String schluessel, String spcd, Integer mand, String sa);

}
