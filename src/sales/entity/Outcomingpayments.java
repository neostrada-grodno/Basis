package sales.entity;
// Generated 25.03.2014 13:42:31 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Outcomingpayments generated by hbm2java
 */
public class Outcomingpayments  implements java.io.Serializable {


     private Integer code;
     private Integer documentCode;
     private Date datetime;
     private Integer amount;
     private String number;
     private String note;

    public Outcomingpayments() {
    }

    public Outcomingpayments(Integer documentCode, Date datetime, Integer amount, String number, String note) {
       this.documentCode = documentCode;
       this.datetime = datetime;
       this.amount = amount;
       this.number = number;
       this.note = note;
    }
   
    public Integer getCode() {
        return this.code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    public Integer getDocumentCode() {
        return this.documentCode;
    }
    
    public void setDocumentCode(Integer documentCode) {
        this.documentCode = documentCode;
    }
    public Date getDatetime() {
        return this.datetime;
    }
    
    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
    public Integer getAmount() {
        return this.amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public String getNumber() {
        return this.number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }




}


