package bedwarsboys.badwars.itemspawner;

import bedwarsboys.badwars.Badwars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class SpawnerConfig {

    //should load from Gameconfig
    ArrayList<Location> copperSpawners = new ArrayList<>();
    ArrayList<Location> ironSpawners = new ArrayList<>();
    ArrayList<Location> goldSpawners = new ArrayList<>();
    ArrayList<Location> specialSpawners = new ArrayList<>();

    ArrayList<BukkitTask> activeSpawners = new ArrayList<>();

    public void startSpawners() {
        activeSpawners.add(Bukkit.getScheduler().runTaskTimer(Badwars.PLUGIN, () -> {
            for (Location l : copperSpawners) {
                Item drop = l.getWorld().spawn(l, Item.class);
                drop.setItemStack(new ItemStack(Material.COPPER_INGOT));
                drop.setVelocity(new Vector(0, 0.1, 0));
            }
        }, 30, 30));
        activeSpawners.add(Bukkit.getScheduler().runTaskTimer(Badwars.PLUGIN, () -> {
            for (Location l : ironSpawners) {
                Item drop = l.getWorld().spawn(l, Item.class);
                drop.setItemStack(new ItemStack(Material.IRON_INGOT));
                drop.setVelocity(new Vector(0, 0.1, 0));
            }
        }, 20 * 8, 20 * 8));
        activeSpawners.add(Bukkit.getScheduler().runTaskTimer(Badwars.PLUGIN, () -> {
            for (Location l : goldSpawners) {
                Item drop = l.getWorld().spawn(l, Item.class);
                drop.setItemStack(new ItemStack(Material.GOLD_INGOT));
                drop.setVelocity(new Vector(0, 0.1, 0));
            }
        }, 20 * 20, 20 * 20));
        activeSpawners.add(Bukkit.getScheduler().runTaskTimer(Badwars.PLUGIN, () -> {
            for (Location l : specialSpawners) {
                Item drop = l.getWorld().spawn(l, Item.class);
                drop.setItemStack(new ItemStack(Material.GUNPOWDER));
                drop.setVelocity(new Vector(0, 0.1, 0));
            }
        }, 20 * 90, 20 * 90));
    }

    public void stopSpawners() {
        for (BukkitTask t : activeSpawners) {
            t.cancel();
        }
        activeSpawners.clear();
    }

    public ArrayList<Location> getCopperSpawners() {
        return copperSpawners;
    }

    public void setCopperSpawners(ArrayList<Location> copperSpawners) {
        this.copperSpawners = copperSpawners;
    }

    public ArrayList<Location> getIronSpawners() {
        return ironSpawners;
    }

    public void setIronSpawners(ArrayList<Location> ironSpawners) {
        this.ironSpawners = ironSpawners;
    }

    public ArrayList<Location> getGoldSpawners() {
        return goldSpawners;
    }

    public void setGoldSpawners(ArrayList<Location> goldSpawners) {
        this.goldSpawners = goldSpawners;
    }

    public ArrayList<Location> getSpecialSpawners() {
        return specialSpawners;
    }

    public void setSpecialSpawners(ArrayList<Location> specialSpawners) {
        this.specialSpawners = specialSpawners;
    }
}
