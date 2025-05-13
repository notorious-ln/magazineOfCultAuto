package com.example.magazineofcultauto;

import java.util.List;

public class ChildItem {
    private String title;             // Заголовок элемента
    private List<GrandchildItem> grandchildItems; // Изменили с Photo на GrandchildItem
    private boolean isExpanded;       // Флаг, раскрыт элемент или нет

    // Конструктор
    public ChildItem(String title, List<GrandchildItem> grandchildItems) {
        this.title = title;
        this.grandchildItems = grandchildItems;
        this.isExpanded = false;      // По умолчанию элемент закрыт
    }

    // Геттеры и сеттеры...

    public String getTitle() {
        return title;
    }

    public List<GrandchildItem> getGrandchildItems() {
        return grandchildItems;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
