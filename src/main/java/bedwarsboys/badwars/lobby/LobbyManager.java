package bedwarsboys.badwars.lobby;

import bedwarsboys.badwars.Badwars;
import bedwarsboys.badwars.WorldBlockArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * This class is able to create a box of barriers, save the area it overrides, unload the box and reload the area.
 */
public class LobbyManager {

    // The location where the lobby spawn is, where the lobby box will be created
    Location lobbyLocation;
    // The location where the lobby box will be, should be backup-ed before placing the box
    WorldBlockArea lobbyBackup;


    public LobbyManager() {
        loadLobbyLocation();
        backupLobbyLocation();
    }

    /**
     * Loads the location of the lobby box from the config
     */
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

    /**
     * Tries to backup the area that the lobby box would overwrite. If the backup is unsuccessful, you should not
     * create the box with createLobbyContainer()
     * @return If the backup was successful.
     */
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

    /**
     * Creates the barrier box at the Location in lobbyLocation. Should only be called if there is a successful
     * backup of the area, as it will be overwritten.
     */
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

    /**
     * Resets the area where the lobby box is to its original state.
     */
    public void removeLobbyContainer() {
        lobbyBackup.pasteArea();
    }

}
