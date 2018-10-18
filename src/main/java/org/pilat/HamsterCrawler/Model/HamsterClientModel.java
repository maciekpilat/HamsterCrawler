/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pilat.HamsterCrawler.Model;

import java.util.List;

/**
 *
 * @author Pilat
 */
public class HamsterClientModel {

    private String Login;
    private String Password;
    
    // COOKIE
    // __RequestVerificationToken_Lw__
    //__cfduid
    //guid
    //rcid
    //RememberMe
    //ChomikSession
    private String requestVerificationTokenLw;
    private String cfduid;
    private String guid;
    private String rcid;
    private String rememberMe;
    private String chomikSession;

    // other
    private String __RequestVerificationToken;
    private String fielId;
    private String SerializedOrgFile;
    private String SerializedUserSelection;
    
    private String urlToDownload;
    
    // podstawowy URL z plikami (do uzyskania wlasciwych url do pobrania)
    private String normalizeHamsterUrl;
    
    // lista numierow id plików do pobrania
    private List idListToDownload;
    
    //lista z URL do pobrania plikow (wlasciwe, pod nimi sa pliki)
    private List urlListToDownload;
    
    // liczba stron z plikami do pobrania
    private int NumberOfFilePages;
    
    // link do pobrania spakowanego pliku
    private String urlToDownloadZipFile;

    @Override
    public String toString() {
        return "HamsterClientModel{" + "Login=" + Login + ", Password=" + Password + ", requestVerificationTokenLw=" + requestVerificationTokenLw + ", cfduid=" + cfduid + ", guid=" + guid + ", rcid=" + rcid + ", rememberMe=" + rememberMe + ", chomikSession=" + chomikSession + ", __RequestVerificationToken=" + __RequestVerificationToken + ", fielId=" + fielId + ", SerializedOrgFile=" + SerializedOrgFile + ", SerializedUserSelection=" + SerializedUserSelection + ", urlToDownload=" + urlToDownload + ", normalizeHamsterUrl=" + normalizeHamsterUrl + ", idListToDownload=" + idListToDownload + ", urlListToDownload=" + urlListToDownload + ", NumberOfFilePages=" + NumberOfFilePages + ", urlToDownloadZipFile=" + urlToDownloadZipFile + '}';
    }

    



    

    /**
     * @return the Login
     */
    public String getLogin() {
        return Login;
    }

    /**
     * @param Login the Login to set
     */
    public void setLogin(String Login) {
        this.Login = Login;
    }

    /**
     * @return the Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * @param Password the Password to set
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }

    /**
     * @return the requestVerificationTokenLw
     */
    public String getRequestVerificationTokenLw() {
        return requestVerificationTokenLw;
    }

    /**
     * @param requestVerificationTokenLw the requestVerificationTokenLw to set
     */
    public void setRequestVerificationTokenLw(String requestVerificationTokenLw) {
        this.requestVerificationTokenLw = requestVerificationTokenLw;
    }

    /**
     * @return the cfduid
     */
    public String getCfduid() {
        return cfduid;
    }

    /**
     * @param cfduid the cfduid to set
     */
    public void setCfduid(String cfduid) {
        this.cfduid = cfduid;
    }

    /**
     * @return the guid
     */
    public String getGuid() {
        return guid;
    }

    /**
     * @param guid the guid to set
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * @return the rcid
     */
    public String getRcid() {
        return rcid;
    }

    /**
     * @param rcid the rcid to set
     */
    public void setRcid(String rcid) {
        this.rcid = rcid;
    }

    /**
     * @return the rememberMe
     */
    public String getRememberMe() {
        return rememberMe;
    }

    /**
     * @param rememberMe the rememberMe to set
     */
    public void setRememberMe(String rememberMe) {
        this.rememberMe = rememberMe;
    }

    /**
     * @return the chomikSession
     */
    public String getChomikSession() {
        return chomikSession;
    }

    /**
     * @param chomikSession the chomikSession to set
     */
    public void setChomikSession(String chomikSession) {
        this.chomikSession = chomikSession;
    }

    /**
     * @return the __RequestVerificationToken
     */
    public String getRequestVerificationToken() {
        return __RequestVerificationToken;
    }

    /**
     * @param __RequestVerificationToken the __RequestVerificationToken to set
     */
    public void setRequestVerificationToken(String __RequestVerificationToken) {
        this.__RequestVerificationToken = __RequestVerificationToken;
    }

    /**
     * @return the fielId
     */
    public String getFielId() {
        return fielId;
    }

    /**
     * @param fielId the fielId to set
     */
    public void setFielId(String fielId) {
        this.fielId = fielId;
    }

    /**
     * @return the SerializedOrgFile
     */
    public String getSerializedOrgFile() {
        return SerializedOrgFile;
    }

    /**
     * @param SerializedOrgFile the SerializedOrgFile to set
     */
    public void setSerializedOrgFile(String SerializedOrgFile) {
        this.SerializedOrgFile = SerializedOrgFile;
    }

    /**
     * @return the SerializedUserSelection
     */
    public String getSerializedUserSelection() {
        return SerializedUserSelection;
    }

    /**
     * @param SerializedUserSelection the SerializedUserSelection to set
     */
    public void setSerializedUserSelection(String SerializedUserSelection) {
        this.SerializedUserSelection = SerializedUserSelection;
    }

    /**
     * @return the urlToDownload
     */
    public String getUrlToDownload() {
        return urlToDownload;
    }

    /**
     * @param urlToDownload the urlToDownload to set
     */
    public void setUrlToDownload(String urlToDownload) {
        this.urlToDownload = urlToDownload;
    }

    /**
     * @return the normalizeHamsterUrl
     */
    public String getNormalizeHamsterUrl() {
        return normalizeHamsterUrl;
    }

    /**
     * @param normalizeHamsterUrl the normalizeHamsterUrl to set
     */
    public void setNormalizeHamsterUrl(String normalizeHamsterUrl) {
        this.normalizeHamsterUrl = normalizeHamsterUrl;
    }

    /**
     * @return the idListToDownload
     */
    public List getIdListToDownload() {
        return idListToDownload;
    }

    /**
     * @param idListToDownload the idListToDownload to set
     */
    public void setIdListToDownload(List idListToDownload) {
        this.idListToDownload = idListToDownload;
    }

    /**
     * @return the urlListToDownload
     */
    public List getUrlListToDownload() {
        return urlListToDownload;
    }

    /**
     * @param urlListToDownload the urlListToDownload to set
     */
    public void setUrlListToDownload(List urlListToDownload) {
        this.urlListToDownload = urlListToDownload;
    }

    /**
     * @return the NumberOfFilePages
     */
    public int getNumberOfFilePages() {
        return NumberOfFilePages;
    }

    /**
     * @param NumberOfFilePages the NumberOfFilePages to set
     */
    public void setNumberOfFilePages(int NumberOfFilePages) {
        this.NumberOfFilePages = NumberOfFilePages;
    }

    /**
     * @return the urlToDownloadZipFile
     */
    public String getUrlToDownloadZipFile() {
        return urlToDownloadZipFile;
    }

    /**
     * @param urlToDownloadZipFile the urlToDownloadZipFile to set
     */
    public void setUrlToDownloadZipFile(String urlToDownloadZipFile) {
        this.urlToDownloadZipFile = urlToDownloadZipFile;
    }
    
   
}
