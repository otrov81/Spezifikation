package app.service;

import app.form.restapiweb.RestApiWeb;
import app.microservice.Microservice;
import app.model.*;
import app.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DBService {

    public final SpeziDetailRepository speziDetailRepository;
    private final SpeziKopfRepository speziKopfRepository;
    private final SpeziTextartRepository speziTextartRepository;
    private final SpeziViewRepository speziViewRepository;
    private final UserRepository userRepository;
    private final SpeziTexteRepository speziTexteRepository;
    private final LoginRepository loginRepository;
    private final SpeziSaRepository speziSaRepository;
    private final SpeziSpcdRepository speziSpcdRepository;
    private final Microservice microservice;
    private final SpeziMemoRepository speziMemoRepository;
    private final SpeziPdfRepository speziPdfRepository;
    private final DocuRepository docuRepository;
    private final RestApiRepository restApiRepository;

    public DBService(SpeziTexteRepository speziTexteRepository, SpeziDetailRepository speziDetailRepository, SpeziKopfRepository speziKopfRepository, SpeziTextartRepository speziTextartRepository, SpeziViewRepository speziViewRepository, UserRepository userRepository, LoginRepository loginRepository, SpeziSaRepository speziSaRepository, SpeziSpcdRepository speziSpcdRepository, Microservice microservice, SpeziMemoRepository speziMemoRepository, SpeziPdfRepository speziPdfRepository, DocuRepository docuRepository, RestApiRepository restApiRepository) {
        this.speziDetailRepository = speziDetailRepository;
        this.speziKopfRepository = speziKopfRepository;
        this.speziTextartRepository = speziTextartRepository;
        this.speziViewRepository = speziViewRepository;
        this.userRepository = userRepository;
        this.speziTexteRepository = speziTexteRepository;
        this.loginRepository = loginRepository;
        this.speziSaRepository = speziSaRepository;
        this.speziSpcdRepository = speziSpcdRepository;
        this.microservice = microservice;
        this.speziMemoRepository = speziMemoRepository;
        this.speziPdfRepository = speziPdfRepository;
        this.docuRepository = docuRepository;
        this.restApiRepository = restApiRepository;
    }

    //user
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public List<SpeziView> SelectAllData() {
        return speziViewRepository.SelectAllData();
    }

    public List<SpeziView> searchBySPCD(String spdc, String kunde, String artikel) {
        return speziViewRepository.searchBySPCD(spdc, kunde, artikel);
    }

    public void deleteSpeziViev(Integer id) {
        speziViewRepository.deleteSpeziViev(id);
    }

    public void updateDataStyle(String cyan, String userNameStyle) {
        userRepository.updateDataStyle(cyan, userNameStyle);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void insertDataUser(String text, String md5, String text1) {
        userRepository.insertDataUser(text, md5, text1);
    }

    public void updateDataUser(String text, String md5, String text1, Integer integer) {
        userRepository.updateDataUser(text, md5, text1, integer);
    }

    public List<SpeziTextart> selectTextart(String spcd) {
        return speziTextartRepository.selectTextart(spcd);
    }

    public List<SpeziDetail> selectSpeziDetail(String sa, String schluessel, String spcd) {
        return speziDetailRepository.selectSpeziDetail(sa, schluessel, spcd);
    }

    public List<SpeziTexte> selectSpeziTexteByKey(String key, String spcd) {
        return speziTexteRepository.selectSpeziTexteByKey(key, spcd);
    }

    public List<SpeziTexte> selectSpeziTexteBlocke(String textart, String spcd) {
        return speziTexteRepository.selectSpeziTexteBlocke(textart, spcd);
    }

    //spezi texte
    public List<SpeziTexte> selectItemKeySpeek(String mand, String textart, String spcd) {
        return speziTexteRepository.selectItemKeySpeek(mand, textart, spcd);
    }

    //spezi texte
    public List<SpeziTexte> selectItemSpezialSearch(String mand, String textart, String spcd, String key) {
        return speziTexteRepository.selectItemSpezialSearch(mand, textart, spcd, key);
    }

    public void saveImet(String mand, String spcd, String textart, String key, String text) {
        speziTexteRepository.saveImet(mand, spcd, textart, key, text);
    }

    public void updateImet(String mand, String spcd, String textart, String text, String key) {
        speziTexteRepository.updateImet(mand, spcd, textart, text, key);
    }

    public void updateSpeziDetail(String text, String key, Integer mand, String sa, String schluessel, String spcd, String znr, String textart) {
        speziDetailRepository.updateSpeziDetail(text, key, mand, sa, schluessel, spcd, znr, textart);
    }

    public void deleteImet(String mand, String spcd, String textart, String key) {
        speziTexteRepository.deleteImet(mand, spcd, textart, key);
    }

    public void insertSpeziDetail(String text, Integer mand, String sa, String schluessel, String spcd, String znr, String textart, String titel) {
        speziDetailRepository.insertSpeziDetail(text, mand, sa, schluessel, spcd, znr, textart, titel);
    }

    public void LogToDatabase(String levelInfo, String message, String username) {
        loginRepository.LogToDatabase(levelInfo, message, username);
    }

    public void deleteSpeziDetail(Integer mand, String sa, String schluessel, String spcd, String znr) {
        speziDetailRepository.deleteSpeziDetail(mand, sa, schluessel, spcd, znr);
    }

    public void updateSpeziDetailTableSort(String znr, Integer mand, String sa, String schluessel, String spcd, String textart) {
        speziDetailRepository.updateSpeziDetailTableSort(znr, mand, sa, schluessel, spcd, textart);
    }

    public List<SpeziSa> selectDropdownSa() {
        return speziSaRepository.selectDropdownSa();
    }

    public List<SpeziSpcd> selectDropdownSpcd() {
        return speziSpcdRepository.selectDropdownSpcd();
    }

    //microservice
    public String getKundenNameFromErp(String kundennr) {
        return microservice.getKundenNameFromErp(kundennr);
    }

    //microservice
    public String getArtikelNameFromErp(String artikelnr) {
        return microservice.getArtikelNameFromErp(artikelnr);
    }

    //microservice
    public String selectIfMemoExist(Integer mand, String sa, String schluessel, String spcd) {
        return speziMemoRepository.selectIfMemoExist(mand, sa, schluessel, spcd);
    }

    //microservice
    public List<String> getLandFromErp() {
        return microservice.getLandFromErp();
    }

    //spezi details
    public void insertSpeziKopfNeuFirst(Integer mand, String sa, String schluessel, String spcd, String artikel, String artbez, String ldc) {
        speziKopfRepository.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);
    }

    //spezi textarten
    public List<SpeziTextart> selectTextartSpcd() {
        return speziTextartRepository.selectTextartSpcd();
    }

    //spezie kopf
    public Integer checkSpeziFirst(String schluessel, String spcd, Integer mand, String sa) {
        return speziKopfRepository.checkSpeziExistence(schluessel, spcd, mand, sa);
    }

    //microservice
    public void getUpdateByLandName(Integer mand, String sa, String schluessel, String spcd, String schluesselLand) {
        microservice.getUpdateByLandName(mand, sa, schluessel, spcd, schluesselLand);
    }

    //microservice
    public void getUpdateByLandNameSpezial(Integer mand, String sa, String schluessel, String spcd, String schluesselLand, String saSpez) {
        microservice.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);
    }

    //spezi detail
    public void deleteSpeziDetailComplet(Integer mand, String sa, String schluessel, String spcd) {
        speziDetailRepository.deleteSpeziDetailComplet(mand, sa, schluessel, spcd);
    }

    //delete kopf
    public void deleteSpeziKopfComplet(Integer mand, String sa, String schluessel, String spcd) {
        speziKopfRepository.deleteSpeziKopfComplet(mand, sa, schluessel, spcd);
    }

    //microservice
    public Boolean checkRABExistence(String kundennr, String artikelnr) {
        return microservice.checkRABExistence(kundennr, artikelnr);
    }

    //detail repository
    public Integer checkKunenNummer(String schluessel, String spcd) {
        return speziDetailRepository.checkKunenNummer(schluessel, spcd);
    }

    //spezi memo
    public void saveAllMemo(Integer mand, String sa, String schluessel, String spcd, String memo) {
        speziMemoRepository.saveAllMemo(mand, sa, schluessel, spcd, memo);
    }

    //spezi memo
    public void updateAllMemo(String memo, Integer mand, String sa, String schluessel, String spcd) {
        speziMemoRepository.updateAllMemo(memo, mand, sa, schluessel, spcd);
    }

    //spezi memo
    public Integer checkIfMemoExist(Integer mand, String sa, String schluessel, String spcd) {
        return speziMemoRepository.checkIfMemoExist(mand, sa, schluessel, spcd);
    }

    //spezi memo
    public void deleteAllMemo(Integer mand, String sa, String schluessel, String spcd) {
        speziMemoRepository.deleteAllMemo(mand, sa, schluessel, spcd);
    }

    //spezi kopf
    public Integer checkSpeziExistenceKunde(String schluessel, String spcd, Integer mand, String sa) {
        return speziKopfRepository.checkSpeziExistenceKunde(schluessel, spcd, mand, sa);
    }

    //spezi kopf
    public Integer checkSpeziExistenceLand(String schluessel, String spcd, Integer mand, String sa) {
        return speziKopfRepository.checkSpeziExistenceLand(schluessel, spcd, mand, sa);
    }

    //spezi textarten
    public void updateTextarteTitel(String titel, Integer mand, String spcd, String znr) {
        speziTextartRepository.updateTextarteTitel(titel, mand, spcd, znr);
    }

    //spezi detail
    public void updateSpeziDetailTableTitel(String titel, Integer mand, String spcd, String znr) {
        speziDetailRepository.updateSpeziDetailTableTitel(titel, mand, spcd, znr);
    }

    //spezi textarten
    public void insetrTextartAll(Integer mand, String spcd, String textart, String znr, String titel) {
        speziTextartRepository.insetrTextartAll(mand, spcd, textart, znr, titel);
    }

    //spezi textart
    public int checkIfZnrExistirt(Integer mand, String spcd, String textart, String znr) {
        return speziTextartRepository.checkIfZnrExistirt(mand, spcd, textart, znr);
    }

    //speti text
    public int checkIfKeyExistirt(Integer mand, String textart, String spcd, String key) {
        return speziTexteRepository.checkIfKeyExistirt(mand, textart, spcd, key);
    }

    //spezi pdf
    public List<SpeziPdf> selecAllPdf() {
        return speziPdfRepository.findAll();
    }

    //spezi pdf
    public List<SpeziPdf> searchByIcon(String logo) {
        return speziPdfRepository.searchByIcon(logo);
    }

    //save docu
    public void saveAllDocu(String docu) {
        docuRepository.saveAllDocu(docu);
    }

    //select docu
    public String selectAllDocu() {
        return docuRepository.selectDocuById().toString();
    }

    //select all restapi
    public List<RestApiModel> selectAllRestApi() {
        return restApiRepository.findAll();
    }

    // Add new restApi and return the saved entity with generated ID
    public RestApiModel addNewRestApi(RestApiModel restApi) {
        return restApiRepository.save(restApi);
    }

    // update restapi
    // Metoda za ažuriranje RestApiModel u bazi podataka
    public void updateRestApi(RestApiModel restApi) {
        Optional<RestApiModel> existingRestApiOptional = restApiRepository.findById(restApi.getId());
        if (existingRestApiOptional.isPresent()) {
            RestApiModel existingRestApi = existingRestApiOptional.get();
            existingRestApi.setMischung(restApi.getMischung());
            existingRestApi.setZut1(restApi.getZut1());
            existingRestApi.setZut2(restApi.getZut2());
            existingRestApi.setZut3(restApi.getZut3());
            restApiRepository.save(existingRestApi);
        } else {
            // Handle error: entity not found
            throw new RuntimeException("RestApi with ID " + restApi.getId() + " not found");
        }
    }

    //delete rastapt
    public void deleteRastApiByID(long id) {
        restApiRepository.deleteById(id);
    }

    //truncate rest ati
    public void truncateRestApi() {
        restApiRepository.truncateRestApi();
    }
}
