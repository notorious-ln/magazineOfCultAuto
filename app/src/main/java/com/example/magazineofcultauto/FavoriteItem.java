package com.example.magazineofcultauto;

public class FavoriteItem {
    private String carId;
    private String carName;
    private String carImage;
    private String carData;

    // Конструктор, геттеры и сеттеры
    public FavoriteItem(String carId, String carName, String carImage, String carData) {
        this.carId = carId;
        this.carName = carName;
        this.carImage = carImage;
        this.carData = carData;
    }

    public String getCarId() { return carId; }
    public String getCarName() { return carName; }
    public String getCarImage() { return carImage; }
    public String getCarData() { return carData; }
}
