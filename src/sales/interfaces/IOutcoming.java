package sales.interfaces;

public interface IOutcoming{
    public void setIncomingItem(int incomingCode, int row);
    public void fixQuantity(int productCode, int incomingCode, int row);
}
