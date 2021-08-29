package jumpingalien.model.storage;

import jumpingalien.model.*;

import java.util.ArrayList;

public class WorldStorage implements GameObjectStorage {

    private Mazub player;
    private ArrayList<GameObject> others = new ArrayList<>();
    private ArrayList<Plant> plants = new ArrayList<>();
    private final World world;

    public WorldStorage(World world) {
        this.world = world;
    }

    public Mazub getPlayer() {
        return player;
    }

    @Override
    public void addMazub(Mazub mazub) {
        if(!world.canHaveAsGameObject(mazub) || world.isTerminated() || world.isStarted())
            throw new IllegalArgumentException("You can not use this object in this situation");
        if(player != null) throw new IllegalArgumentException("You can not have multiple aliens");
        player = mazub;
        world.setWindow(world.getVisibleWindowWidth(), world.getVisibleWindowHeight());
        mazub.setWorld(world);
    }

    @Override
    public void removeMazub(Mazub mazub) {
        if (!world.hasProperGameObject(mazub)) throw new IllegalArgumentException("You can not use this object");
        player = null;
        mazub.unSetWorld();
    }

    @Override
    public void addSkullcab(Skullcab skullcab) {
        addPlant(skullcab);
    }

    @Override
    public void removeSkullcab(Skullcab skullcab) {
        removePlant(skullcab);
    }

    @Override
    public void addSneezewort(Sneezewort sneezewort) {
        addPlant(sneezewort);
    }

    @Override
    public void removeSneezewort(Sneezewort sneezewort) {
        removePlant(sneezewort);
    }

    @Override
    public void addSlime(Slime slime) {
        if(!world.canHaveAsGameObject(slime) || world.isTerminated() || world.isStarted())
            throw new IllegalArgumentException("You can not use this object in this situation");
        others.add(slime);
        if(slime.getSchool() != null)
            slime.getSchool().setWorld(world);
        slime.setWorld(world);
    }

    @Override
    public void removeSlime(Slime slime) {
        if (!world.hasProperGameObject(slime)) throw new IllegalArgumentException("You can not use this object");
        others.remove(slime);
        if(slime.getSchool() != null && slime.getSchool().getSlimes().size() == 1)
            slime.getSchool().setWorld(null);
        slime.unSetWorld();
    }

    @Override
    public void addShark(Shark shark) {
        addOther(shark);
    }

    @Override
    public void removeShark(Shark shark) {
        removeOther(shark);
    }

    @Override
    public void addSpider(Spider spider) {
        addOther(spider);
    }

    @Override
    public void removeSpider(Spider spider) {
        removeOther(spider);
    }

    private void addPlant(Plant plant){
        if(!world.canHaveAsGameObject(plant) || world.isTerminated() || world.isStarted())
            throw new IllegalArgumentException("You can not use this object in this situation");
        plants.add(plant);
        plant.setWorld(world);
    }

    private void removePlant(Plant plant){
        if (!world.hasProperGameObject(plant)) throw new IllegalArgumentException("You can not use this object");
        plants.remove(plant);
        plant.unSetWorld();
    }

    private void addOther(GameObject gameObject){
        if(!world.canHaveAsGameObject(gameObject) || world.isTerminated() || world.isStarted())
            throw new IllegalArgumentException("You can not use this object in this situation");
        others.add(gameObject);
        gameObject.setWorld(world);
    }

    private void removeOther(GameObject gameObject){
        if (!world.hasProperGameObject(gameObject)) throw new IllegalArgumentException("You can not use this object");
        others.remove(gameObject);
        gameObject.unSetWorld();
    }

    public ArrayList<GameObject> getAllGameObjects() {
        ArrayList<GameObject> gameObjects = new ArrayList<>();
        if(player != null) gameObjects.add(player);
        gameObjects.addAll(others);
        gameObjects.addAll(plants);
        return gameObjects;
    }

    public ArrayList<GameObject> getOthers() {
        ArrayList<GameObject> noPlants = new ArrayList<>();
        if(player != null) noPlants.add(player);
        noPlants.addAll(others);
        return noPlants;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }
}
