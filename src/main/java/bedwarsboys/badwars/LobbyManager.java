package bedwarsboys.badwars;

import bedwarsboys.badwars.WorldBlockArea.VirtualBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class LobbyManager {

    Location lobbyLocation;
    WorldBlockArea lobbyBackup;

    static ArrayList<Location> copperSpawner = new ArrayList<>();
    static ArrayList<Location> ironSpawner = new ArrayList<>();
    static ArrayList<Location> goldSpawner = new ArrayList<>();
    static ArrayList<Location> specialSpawner = new ArrayList<>();

    static ArrayList<BukkitTask> activeSpawners = new ArrayList<>();

    public LobbyManager() {
        loadLobbyLocation();
        backupLobbyLocation();
    }

    public boolean loadSpawners() {
        copperSpawner.clear();
        ironSpawner.clear();
        goldSpawner.clear();
        WorldBlockArea arena = new WorldBlockArea();
        if (arena.loadBlockArea("arena")) {
            for (VirtualBlock[][] b2 : arena.blocks) {
                for (VirtualBlock[] b1 : b2) {
                    for (VirtualBlock b : b1) {
                        if (b.blockData.getMaterial() == Material.COPPER_BLOCK) {
                            copperSpawner.add(b.location.clone().add(0.5, 1.5, 0.5));
                            //Bukkit.getLogger().info("GOT COPPER BLOCK");
                        }
                        if (b.blockData.getMaterial() == Material.IRON_BLOCK) {
                            ironSpawner.add(b.location.clone().add(0.5, 1.5, 0.5));
                        }
                        if (b.blockData.getMaterial() == Material.GOLD_BLOCK) {
                            goldSpawner.add(b.location.clone().add(0.5, 1.5, 0.5));
                        }
                        if (b.blockData.getMaterial() == Material.CALCITE) {
                            specialSpawner.add(b.location.clone().add(0.5, 1.5, 0.5));
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void startSpawners() {
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

    public void stopSpawners() {
        for (BukkitTask t : activeSpawners) {
            t.cancel();
        }
        activeSpawners.clear();
    }

    void loadLobbyLocation() {
        int x, y, z;
        String w;
        if (Badwars.CONFIG.isSet("lobby")) {
            x = Badwars.CONFIG.getInt("lobby.x");
            y = Badwars.CONFIG.getInt("lobby.y");
            z = Badwars.CONFIG.getInt("lobby.z");
            w = Badwars.CONFIG.getString("lobby.w");
            if (w != null) {
                if (Bukkit.getWorld(w) == null) {
                    return;
                }
                lobbyLocation = new Location(Bukkit.getWorld(w), x, y, z);
            }
        }
    }

    boolean backupLobbyLocation() {
        if (lobbyLocation == null) {
            Bukkit.getLogger().info("Lobby location not set.");
            loadLobbyLocation();
            if (lobbyLocation == null) return false;
        }
        lobbyBackup = new WorldBlockArea(lobbyLocation.clone().add(-4, -1, -4), lobbyLocation.clone().add(4, 5, 4));
        if (!lobbyBackup.saveBlockArea("lobby_area_orig", true)) {
            Bukkit.getLogger().info(Badwars.PLUGIN_NAME + "Couldn't backup the lobby area!");
        } else {
            //we were able to backup the area, perfect.
            return true;
        }
        return false;
    }

    public void createLobbyContainer() {
        if (lobbyLocation == null) {
            Bukkit.getLogger().info("Lobby location not set.");
            loadLobbyLocation();
            if (lobbyLocation == null) return;
        }
        if (!backupLobbyLocation()) {
            Bukkit.getLogger().info("Will not create container as backup of original area failed. Try reloading the plugin.");
            return;
        }
        Material b = Material.BARRIER;
        int minX = lobbyLocation.getBlockX() - 4;
        int minY = lobbyLocation.getBlockY() - 1;
        int minZ = lobbyLocation.getBlockZ() - 4;
        int maxX = lobbyLocation.getBlockX() + 4;
        int maxY = lobbyLocation.getBlockY() + 5;
        int maxZ = lobbyLocation.getBlockZ() + 4;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ) {
                        lobbyLocation.getWorld().getBlockAt(x, y, z).setType(b);
                    } else {
                        lobbyLocation.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                    }

                }
            }
        }
    }

    public void removeLobbyContainer() {
        lobbyBackup.pasteArea();
    }

}
