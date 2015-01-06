package sales.entity;
// Generated 25.03.2014 13:42:31 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Service generated by hbm2java
 */
public class Service  implements java.io.Serializable {


     private Integer code;
     private Date datetime;
     private String number;
     private Integer employeeCode;
     private Integer contractorCode;
     private Integer locked;
     private int active;

    public Service() {
    }

	
    public Service(int active) {
        this.active = active;
    }
    public Service(Date datetime, String number, Integer employeeCode, Integer contractorCode, Integer locked, int active) {
       this.datetime = datetime;
       this.number = number;
       this.employeeCode = employeeCode;
       this.contractorCode = contractorCode;
       this.locked = locked;
       this.active = active;
    }
   
    public Integer getCode() {
        return this.code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    public Date getDatetime() {
        return this.datetime;
    }
    
    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
    public String getNumber() {
        return this.number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    public Integer getEmployeeCode() {
        return this.employeeCode;
    }
    
    public void setEmployeeCode(Integer employeeCode) {
        this.employeeCode = employeeCode;
    }
    public Integer getContractorCode() {
        return this.contractorCode;
    }
    
    public void setContractorCode(Integer contractorCode) {
        this.contractorCode = contractorCode;
    }
    public Integer getLocked() {
        return this.locked;
    }
    
    public void setLocked(Integer locked) {
        this.locked = locked;
    }
    public int getActive() {
        return this.active;
    }
    
    public void setActive(int active) {
        this.active = active;
    }




}


