package app.repository;

import app.model.SpeziMemo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeziMemoRepository extends JpaRepository<SpeziMemo, Integer> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `Spezi`.`tblspez_memo` (`mand`, `sa`, `schluessel`, `spcd`, `memo`) VALUES (?1, ?2, ?3, ?4, ?5)",nativeQuery = true )
    void saveAllMemo(Integer mand, String sa, String schluessel, String spcd, String memo);
    @Transactional
    @Modifying
    @Query(value = "UPDATE `Spezi`.`tblspez_memo` SET `memo`=?1 WHERE `mand`=?2 AND `sa`=?3 AND `schluessel`=?4 AND `spcd`=?5",nativeQuery = true )
    void updateAllMemo(String memo, Integer mand, String sa, String schluessel, String spcd);

    @Query(value = "SELECT COUNT(*) FROM `Spezi`.`tblspez_memo` WHERE `mand`=?1 AND `sa`=?2 AND `schluessel`=?3 AND `spcd`=?4",nativeQuery = true )
    Integer checkIfMemoExist(Integer mand, String sa, String schluessel, String spcd);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM `Spezi`.`tblspez_memo` WHERE `mand`=?1 AND `sa`=?2 AND `schluessel`=?3 AND `spcd`=?4",nativeQuery = true )
    void deleteAllMemo(Integer mand, String sa, String schluessel, String spcd);

    @Query(value = "SELECT `memo` FROM `Spezi`.`tblspez_memo` WHERE `mand`=?1 AND `sa`=?2 AND `schluessel`=?3 AND `spcd`=?4",nativeQuery = true )
    String selectIfMemoExist(Integer mand, String sa, String schluessel, String spcd);

}
