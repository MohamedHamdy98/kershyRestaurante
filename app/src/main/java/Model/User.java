package Model;

import android.widget.Switch;

import java.io.Serializable;

public class User {
    private String id;
    private String name;
    private String numItem;
    private String price;
    private boolean isShrink = false;
    private String nameUser;
    private String imageURL;
    private String Phone;
    private String UserName;
    private String AddressWrite;
    private String totalPrice;
    private String TotalPriceToPay;
    private boolean writeOrder;
    private boolean wayOrder;
    private boolean preparingOrder;
    private boolean deliveredOrder;

    public User() {
    }

    public User(String nameUser, String imageURL, String Phone) {
        this.nameUser = nameUser;
        this.imageURL = imageURL;
        this.Phone = Phone;
    }

    public User(String nameUser, String imageURL,
                String phone, String userName, String addressWrite,
                String totalPrice, String totalPriceToPay, boolean writeOrder,
                boolean wayOrder, boolean preparingOrder, boolean deliveredOrder,
                Switch writeSwitch) {
        this.nameUser = nameUser;
        this.imageURL = imageURL;
        Phone = phone;
        UserName = userName;
        AddressWrite = addressWrite;
        this.totalPrice = totalPrice;
        TotalPriceToPay = totalPriceToPay;
        this.writeOrder = writeOrder;
        this.wayOrder = wayOrder;
        this.preparingOrder = preparingOrder;
        this.deliveredOrder = deliveredOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumItem() {
        return numItem;
    }

    public void setNumItem(String numItem) {
        this.numItem = numItem;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }

    public boolean isWriteOrder() {
        return writeOrder;
    }

    public void setWriteOrder(boolean writeOrder) {
        this.writeOrder = writeOrder;
    }

    public boolean isWayOrder() {
        return wayOrder;
    }

    public void setWayOrder(boolean wayOrder) {
        this.wayOrder = wayOrder;
    }

    public boolean isPreparingOrder() {
        return preparingOrder;
    }

    public void setPreparingOrder(boolean preparingOrder) {
        this.preparingOrder = preparingOrder;
    }

    public boolean isDeliveredOrder() {
        return deliveredOrder;
    }

    public void setDeliveredOrder(boolean deliveredOrder) {
        this.deliveredOrder = deliveredOrder;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalPriceToPay() {
        return TotalPriceToPay;
    }

    public void setTotalPriceToPay(String totalPriceToPay) {
        TotalPriceToPay = totalPriceToPay;
    }

    public String getAddressWrite() {
        return AddressWrite;
    }

    public void setAddressWrite(String addressWrite) {
        AddressWrite = addressWrite;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }
}
