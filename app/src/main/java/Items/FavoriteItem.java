package Items;

import org.json.JSONException;
import org.json.JSONObject;

public class FavoriteItem {
    private String itemId;
    private String itemName;
    private String itemImage;
    private String itemData;
    private double itemPrice;

    private String itemCategory;

    public FavoriteItem(String itemId, String itemName, String itemImage, String itemData, double itemPrice, String itemCategory) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemData = itemData;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
    }

    public String getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getItemImage() { return itemImage; }
    public String getItemData() { return itemData; }
    public double getItemPrice() { return itemPrice; }
    public String getItemCategory() { return itemCategory; }
    public String getSize() {
        try {
            JSONObject json = new JSONObject(itemData);
            return json.getString("size");
        } catch (JSONException e) {
            return "N/A";
        }
    }
}