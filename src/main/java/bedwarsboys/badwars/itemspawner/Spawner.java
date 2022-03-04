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

public class Spawner {
    //should load from Gameconfig
    static ArrayList<Location> copperSpawner = new ArrayList<>();
    static ArrayList<Location> ironSpawner = new ArrayList<>();
    static ArrayList<Location> goldSpawner = new ArrayList<>();
    static ArrayList<Location> specialSpawner = new ArrayList<>();

    static ArrayList<BukkitTask> activeSpawners = new ArrayList<>();

    public static void startSpawners() {
        activeSpawners.add(Bukkit.getScheduler().runTaskTimer(Badwars.PLUGIN, () -> {
            for (Location l : copperSpawner) {
                Item drop = l.getWorld().spawn(l, Item.class);
                drop.setItemStack(new ItemStack(Material.COPPER_INGOT));
                drop.setVelocity(new Vector(0, 0.1, 0));
            }
        }, 30, 30));
        activeSpawners.add(Bukkit.getScheduler().runTaskTimer(Badwars.PLUGIN, () -> {
            for (Location l : ironSpawner) {
                Item drop = l.getWorld().spawn(l, Item.class);
                drop.setItemStack(new ItemStack(Material.IRON_INGOT));
                drop.setVelocity(new Vector(0, 0.1, 0));
            }
        }, 20*8, 20*8));
        activeSpawners.add(Bukkit.getScheduler().runTaskTimer(Badwars.PLUGIN, () -> {
            for (Location l : goldSpawner) {
                Item drop = l.getWorld().spawn(l, Item.class);
                drop.setItemStack(new ItemStack(Material.GOLD_INGOT));
                drop.setVelocity(new Vector(0, 0.1, 0));
            }
        }, 20*20, 20*20));
        activeSpawners.add(Bukkit.getScheduler().runTaskTimer(Badwars.PLUGIN, () -> {
            for (Location l : specialSpawner) {
                Item drop = l.getWorld().spawn(l, Item.class);
                drop.setItemStack(new ItemStack(Material.GUNPOWDER));
                drop.setVelocity(new Vector(0, 0.1, 0));
            }
        }, 20*90, 20*90));
    }
    public static void stopSpawners() {
        for (BukkitTask t : activeSpawners) {
            t.cancel();
        }
        activeSpawners.clear();
    }
}
