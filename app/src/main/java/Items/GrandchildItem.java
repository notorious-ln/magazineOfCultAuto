package Items;

import java.util.ArrayList;
import java.util.List;

public class GrandchildItem {
    private String title;
    private List<Photo> photos;
    private boolean isExpanded;

    public GrandchildItem(String title, List<Photo> photos) {
        this.title = title;
        this.photos = photos  != null ? photos : new ArrayList<>();;
        this.isExpanded = false;
    }

    // Геттеры и сеттеры
    public String getTitle() {
        return title;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}