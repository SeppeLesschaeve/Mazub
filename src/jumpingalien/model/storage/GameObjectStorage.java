package jumpingalien.model.storage;

import jumpingalien.model.*;

import java.util.ArrayList;

public interface GameObjectStorage {

    void addMazub(Mazub mazub);
    void removeMazub(Mazub mazub);
    void addSkullcab(Skullcab skullcab);
    void removeSkullcab(Skullcab skullcab);
    void addSneezewort(Sneezewort sneezewort);
    void removeSneezewort(Sneezewort sneezewort);
    void addSlime(Slime slime);
    void removeSlime(Slime slime);
    void addShark(Shark shark);
    void removeShark(Shark shark);
    void addSpider(Spider spider);
    void removeSpider(Spider spider);
    ArrayList<GameObject> getAllGameObjects();
}
