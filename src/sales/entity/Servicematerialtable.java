package sales.entity;
// Generated 25.03.2014 13:42:31 by Hibernate Tools 3.2.1.GA



/**
 * Servicematerialtable generated by hbm2java
 */
public class Servicematerialtable  implements java.io.Serializable {


     private Integer code;
     private Integer documentCode;
     private Integer productCode;
     private int quantity;
     private int price;
     private int amount;

    public Servicematerialtable() {
    }

	
    public Servicematerialtable(int quantity, int price, int amount) {
        this.quantity = quantity;
        this.price = price;
        this.amount = amount;
    }
    public Servicematerialtable(Integer documentCode, Integer productCode, int quantity, int price, int amount) {
       this.documentCode = documentCode;
       this.productCode = productCode;
       this.quantity = quantity;
       this.price = price;
       this.amount = amount;
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
    public Integer getProductCode() {
        return this.productCode;
    }
    
    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }
    public int getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getPrice() {
        return this.price;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    public int getAmount() {
        return this.amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }




}

