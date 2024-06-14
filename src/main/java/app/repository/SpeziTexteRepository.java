package app.repository;

import app.model.SpeziTexte;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeziTexteRepository extends JpaRepository<SpeziTexte, Integer> {
    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM  tblspez_texte tl  WHERE tl.key = ?1 AND tl.spcd=?2", nativeQuery = true)
    List<SpeziTexte> selectSpeziTexteByKey(String key, String spcd);

    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM  tblspez_texte tl  WHERE tl.textart = ?1 AND tl.spcd=?2", nativeQuery = true)
    List<SpeziTexte> selectSpeziTexteBlocke(String textart, String spcd);

    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM  tblspez_texte tl WHERE tl.mand=?1 AND tl.textart=?2 AND spcd=?3", nativeQuery = true)
    List<SpeziTexte> selectItemKeySpeek(String mand, String textart, String spcd);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `Spezi`.`tblspez_texte` (`mand`, `spcd`, `textart`, `key`, `text`) VALUES (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    void saveImet(String mand,  String spcd, String textart, String key, String text);



    @Transactional
    @Modifying
    @Query(value = "UPDATE `Spezi`.`tblspez_texte` SET `mand`=:mand, `spcd`=:spcd, `textart`=:textart, `text`=:text WHERE `mand`=:mand AND `spcd`=:spcd AND `textart`=:textart AND `key`=:key", nativeQuery = true)
    void updateImet(@Param("mand") String mand, @Param("spcd") String spcd, @Param("textart") String textart, @Param("text") String text, @Param("key") String key);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM `Spezi`.`tblspez_texte` WHERE `mand`=:mand AND `spcd`=:spcd AND `textart`=:textart AND `key`=:key", nativeQuery = true)
    void deleteImet(@Param("mand") String mand, @Param("spcd") String spcd, @Param("textart") String textart, @Param("key") String key);


    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM  tblspez_texte tl WHERE " +
            " (tl.mand LIKE %?1% OR COALESCE(?1, '') = '')" +
            " AND (tl.textart LIKE %?2% OR COALESCE(?2, '') = '') " +
            " AND (tl.spcd = ?3 OR COALESCE(?3, '') = '')" +
            " AND (tl.`key` LIKE %?4% OR COALESCE(?4, '') = '')" , nativeQuery = true)
    List<SpeziTexte> selectItemSpezialSearch(String mand, String textart, String spcd, String key);

    @Query(value = "SELECT COUNT(*) FROM  tblspez_texte tl WHERE tl.mand=?1 AND tl.textart=?2 AND tl.spcd=?3 AND `key` =?4", nativeQuery = true)
    int checkIfKeyExistirt(Integer mand, String textart, String spcd, String key);


}
