/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pilat.HamsterCrawler.Model;

/**
 *
 * @author Pilat
 */
public class HamsterClientModel {

    private String Login;
    private String Password;
    // COOKIE
    // __RequestVerificationToken_Lw__
    // ChomikSession
    // RememberMe
    // rcid
    // guid
    // __cfduid
    private String requestVerificationTokenLw;
    private String chomikSession;
    private String rememberMe;
    private String rcid;        
    private String guid;
    private String cfduid;
    // other
    private String __RequestVerificationToken;
    private String fielId;
    private String SerializedOrgFile;
    private String SerializedUserSelection;

    @Override
    public String toString() {
        return "HamsterClientModel{" + "Login=" + Login + ", Password=" + Password + ", requestVerificationTokenLw=" + requestVerificationTokenLw + ", chomikSession=" + chomikSession + ", rememberMe=" + rememberMe + ", rcid=" + rcid + ", guid=" + guid + ", cfduid=" + cfduid + ", __RequestVerificationToken=" + __RequestVerificationToken + ", fielId=" + fielId + ", SerializedOrgFile=" + SerializedOrgFile + ", SerializedUserSelection=" + SerializedUserSelection + '}';
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

    
    
    

    
}
