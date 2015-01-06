package sales.interfaces;

public interface IProductItem {
    public void addProductItem(Integer productCode);
    public void setProductItem(Integer productCode, int row);
    public void setProductItemPart(Integer productCode, int row);
}
