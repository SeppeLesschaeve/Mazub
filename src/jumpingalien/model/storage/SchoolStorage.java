package jumpingalien.model.storage;

import jumpingalien.model.*;

import java.util.*;

public class SchoolStorage implements GameObjectStorage{

    private TreeMap<Long, Slime> slimes = new TreeMap<>();
    private School school;

    public SchoolStorage(School school) {
        this.school = school;
    }

    @Override
    public void addMazub(Mazub mazub) {

    }

    @Override
    public void removeMazub(Mazub mazub) {

    }

    @Override
    public void addSkullcab(Skullcab skullcab) {

    }

    @Override
    public void removeSkullcab(Skullcab skullcab) {

    }

    @Override
    public void addSneezewort(Sneezewort sneezewort) {

    }

    @Override
    public void removeSneezewort(Sneezewort sneezewort) {

    }

    @Override
    public void addSlime(Slime slime) {
        slime.setSchool(school);
        slimes.put(slime.getId(), slime);
    }

    @Override
    public void removeSlime(Slime slime) {
        slime.setSchool(null);
        slimes.remove(slime.getId());
    }

    @Override
    public void addShark(Shark shark) {

    }

    @Override
    public void removeShark(Shark shark) {

    }

    @Override
    public void addSpider(Spider spider) {

    }

    @Override
    public void removeSpider(Spider spider) {

    }

    @Override
    public ArrayList<GameObject> getAllGameObjects() {
        ArrayList<GameObject> school = new ArrayList<>();
        Iterator<Slime> currentSchool = slimes.values().iterator();
        currentSchool.forEachRemaining(slime -> school.add(slime));
        return school;
    }

    public TreeMap<Long, Slime> getSlimes(){
        return slimes;
    }

    public void remove() {
        this.slimes = null;
    }
}
