package jumpingalien.model.collision;

import jumpingalien.model.*;

public interface Collidable {
    void collideWithMazub(Mazub mazub);
    void collideWithShark(Shark shark);
    void collideWithSkullcab(Skullcab skullcab);
    void collideWithSlime(Slime slime);
    void collideWithSneezewort(Sneezewort sneezewort);
    void collideWithSpider(Spider spider);
}
