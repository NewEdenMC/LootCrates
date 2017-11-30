package co.neweden.LootCrates;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import static co.neweden.LootCrates.Chances.randomDespawnTime;
import static co.neweden.LootCrates.ChestSpawner.newChest;
import static co.neweden.LootCrates.Database.*;
import static co.neweden.LootCrates.Main.debugActive;

public class Timer{
 private static Main plugin;

    Timer(Main pl) {
        plugin = pl;
    }

    static void OnCrateCreated(Block block){
        long GameTicks = randomDespawnTime();
        debugActive(false,"The crate is about to despawn in: " + (GameTicks/20)  + " secs", null);

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                DespawnChest(block, false),GameTicks);

    }

    public static void OnCrateCreated(Block block, long timeWanted) {
        debugActive(false, "The crate is about to despawn in: " + (timeWanted / 20) + " secs", null);

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                DespawnChest(block, false), timeWanted);

    }

    static void DespawnChest(Block block, boolean noRespawn) {
        ChestClass chClass = getCrateFromHashMap(block);
        if (chClass == null) return;

        //Despawn Chests
        World world = Bukkit.getWorld(chClass.world);
        if (world == null) return;
        Location chestLoc = new Location(world, chClass.x, chClass.y, chClass.z);
        Chest ch = (Chest) chestLoc.getBlock().getState();
        Inventory ChestInv = ch.getInventory();
        ChestInv.clear();
        ch.setType(Material.AIR);
        removeCrateFromHashMap(block);
        if (!noRespawn) {
            newChest(chClass.num, true, false);
        }

    }
}
