package app.repository;

import app.model.SpeziView;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeziViewRepository extends JpaRepository<SpeziView, Integer> {
    @Transactional
    @Modifying
    @Query(value = "SELECT tblspez_kopf.id AS ID,tblspez_kopf.mand AS Mand, tblspez_kopf.sa AS SA, tblspez_kopf.schluessel AS Schluessel, " +
            "tblspez_kopf.spcd AS SPCD, tblspez_kopf.kunde AS Kunde, tblspez_kopf.artikel AS Artikel, tblspez_kopf.ldc AS ldc, " +
            "tblspez_kopf.edt AS EDT, tblspez_kopf.aedt AS AEDT, MAX(tblspez_detail.aedt) AS MaxvonAEDT, " +
            "IF(kunden.name IS NULL, '', CONCAT_WS(TRIM(kunden.name), ', ', TRIM(kunden.ort))) AS KDNam, " +
            "artikel.name AS Artbez " +
            "FROM tblspez_kopf " +
            "JOIN tblspez_detail ON tblspez_kopf.mand = tblspez_detail.mand AND " +
            "tblspez_kopf.sa = tblspez_detail.sa AND " +
            "tblspez_kopf.schluessel = tblspez_detail.schluessel AND " +
            "tblspez_kopf.spcd = tblspez_detail.spcd " +
            "LEFT JOIN erp.kunden ON tblspez_kopf.kunde = kunden.kundennr AND tblspez_kopf.mand = kunden.mandant " +
            "LEFT JOIN erp.artikel ON tblspez_kopf.artikel = artikel.artikelnr AND tblspez_kopf.mand = artikel.mandant " +
            "WHERE tblspez_kopf.mand = '1000'" +
            "GROUP BY tblspez_kopf.mand, tblspez_kopf.sa, tblspez_kopf.schluessel, tblspez_kopf.spcd, " +
            "tblspez_kopf.kunde, tblspez_kopf.artikel, tblspez_kopf.edt, tblspez_kopf.aedt " +
            "ORDER BY tblspez_kopf.sa, tblspez_kopf.schluessel " +
            "LIMIT 10", nativeQuery = true)

    List<SpeziView> SelectAllData();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM `Spezi`.`tblspez_kopf` WHERE  `id`=?1", nativeQuery = true)
    void deleteSpeziViev(Integer id);

    @Transactional
    @Modifying
    @Query(value = "SELECT tblspez_kopf.id AS ID,tblspez_kopf.mand AS Mand, tblspez_kopf.sa AS SA, tblspez_kopf.schluessel AS Schluessel, " +
            "tblspez_kopf.spcd AS SPCD, tblspez_kopf.kunde AS Kunde, tblspez_kopf.artikel AS Artikel, tblspez_kopf.ldc AS ldc, " +
            "tblspez_kopf.edt AS EDT, tblspez_kopf.aedt AS AEDT, MAX(tblspez_detail.aedt) AS MaxvonAEDT, " +
            "IF(kunden.name IS NULL, '', CONCAT_WS(TRIM(kunden.name), ', ', TRIM(kunden.ort))) AS KDNam, " +
            "artikel.name AS Artbez " +
            "FROM tblspez_kopf " +
            "JOIN tblspez_detail ON tblspez_kopf.mand = tblspez_detail.mand AND " +
            "tblspez_kopf.sa = tblspez_detail.sa AND " +
            "tblspez_kopf.schluessel = tblspez_detail.schluessel AND " +
            "tblspez_kopf.spcd = tblspez_detail.spcd " +
            "LEFT JOIN erp.kunden ON tblspez_kopf.kunde = kunden.kundennr AND tblspez_kopf.mand = kunden.mandant " +
            "LEFT JOIN erp.artikel ON tblspez_kopf.artikel = artikel.artikelnr AND tblspez_kopf.mand = artikel.mandant " +
            "WHERE tblspez_kopf.mand = '1000' AND (tblspez_kopf.spcd = ?1 OR COALESCE(?1, '') = '') "  +
            " AND (tblspez_kopf.kunde LIKE %?2% OR COALESCE(?2, '') = '') " +
            " AND (tblspez_kopf.artikel LIKE %?3% OR COALESCE(?3, '') = '') " +
            "GROUP BY tblspez_kopf.mand, tblspez_kopf.sa, tblspez_kopf.schluessel, tblspez_kopf.spcd, " +
            "tblspez_kopf.kunde, tblspez_kopf.artikel, tblspez_kopf.edt, tblspez_kopf.aedt " +
            "ORDER BY tblspez_kopf.sa, tblspez_kopf.schluessel " +
            "LIMIT 100", nativeQuery = true)
    List<SpeziView> searchBySPCD(String spcd, String kunde, String artikel);


}
