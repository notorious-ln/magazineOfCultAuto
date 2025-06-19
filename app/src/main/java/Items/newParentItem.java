package Items;

import java.util.List;

public class newParentItem {
    private String title;                 // Заголовок категории / раздела
    private List<ChildItem> childItems;  // Список дочерних элементов второго уровня
    private boolean isExpanded;           // Флаг раскрытия (анимация)

    public newParentItem(String title, List<ChildItem> childItems) {
        this.title = title;
        this.childItems = childItems;
        this.isExpanded = false;          // По умолчанию список свернут
    }

    // Геттеры и сеттеры...

    public String getTitle() {
        return title;
    }

    public List<ChildItem> getChildItems() {
        return childItems;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
