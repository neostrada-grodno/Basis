package sales.entity;
// Generated 25.03.2014 13:42:31 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Contractors generated by hbm2java
 */
public class Contractors  implements java.io.Serializable {


     private Integer code;
     private String contractNumber;
     private Date contractDate;
     private String name;
     private String address;
     private String unloadingAddress;
     private String email;
     private String account;
     private Integer bank;
     private String unt;
     private String head;
     private String headPositionName;
     private String primaryDocument;
     private String note;
     private int locked;
     private int active;

    public Contractors() {
    }

	
    public Contractors(int locked, int active) {
        this.locked = locked;
        this.active = active;
    }
    public Contractors(String contractNumber, Date contractDate, String name, String address, String unloadingAddress, String email, String account, Integer bank, String unt, String head, String headPositionName, String primaryDocument, String note, int locked, int active) {
       this.contractNumber = contractNumber;
       this.contractDate = contractDate;
       this.name = name;
       this.address = address;
       this.unloadingAddress = unloadingAddress;
       this.email = email;
       this.account = account;
       this.bank = bank;
       this.unt = unt;
       this.head = head;
       this.headPositionName = headPositionName;
       this.primaryDocument = primaryDocument;
       this.note = note;
       this.locked = locked;
       this.active = active;
    }
   
    public Integer getCode() {
        return this.code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getContractNumber() {
        return this.contractNumber;
    }
    
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
    public Date getContractDate() {
        return this.contractDate;
    }
    
    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public String getUnloadingAddress() {
        return this.unloadingAddress;
    }
    
    public void setUnloadingAddress(String unloadingAddress) {
        this.unloadingAddress = unloadingAddress;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAccount() {
        return this.account;
    }
    
    public void setAccount(String account) {
        this.account = account;
    }
    public Integer getBank() {
        return this.bank;
    }
    
    public void setBank(Integer bank) {
        this.bank = bank;
    }
    public String getUnt() {
        return this.unt;
    }
    
    public void setUnt(String unt) {
        this.unt = unt;
    }
    public String getHead() {
        return this.head;
    }
    
    public void setHead(String head) {
        this.head = head;
    }
    public String getHeadPositionName() {
        return this.headPositionName;
    }
    
    public void setHeadPositionName(String headPositionName) {
        this.headPositionName = headPositionName;
    }
    public String getPrimaryDocument() {
        return this.primaryDocument;
    }
    
    public void setPrimaryDocument(String primaryDocument) {
        this.primaryDocument = primaryDocument;
    }
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    public int getLocked() {
        return this.locked;
    }
    
    public void setLocked(int locked) {
        this.locked = locked;
    }
    public int getActive() {
        return this.active;
    }
    
    public void setActive(int active) {
        this.active = active;
    }




}


