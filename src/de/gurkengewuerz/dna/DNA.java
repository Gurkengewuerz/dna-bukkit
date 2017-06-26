package de.gurkengewuerz.dna;

import de.gurkengewuerz.dna.commands.DNACmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author gurkengewuerz.de
 */
public class DNA extends JavaPlugin {

    public static String prefix = "§6»§7[§bDNA§7]§e  ";
    public static ArrayList<Player> players = new ArrayList<>();
    public static HashMap<Player, Location> newLoc = new HashMap<>();
    public static HashMap<Player, Location> oldLoc = new HashMap<>();
    public static HashMap<Location, Integer> dataValuesLoc = new HashMap<>();
    private static DNA instance;

    @Override
    public void onEnable() {
        instance = this;
        registerEvents();
        registerCommands();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player p : players) {
                try { // Fehler? welche Fehler?
                    if (getDistance(p.getLocation(), new Location(newLoc.get(p).getWorld(), newLoc.get(p).getX(), newLoc.get(p).getY() + 2, newLoc.get(p).getZ())) <= 1 && p.isOnGround()) {
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                            Location lastLoc = newLoc.get(p);
                            newLoc.remove(p);
                            if (oldLoc.containsKey(p)) {
                                oldLoc.get(p).getBlock().setType(Material.AIR);
                                oldLoc.remove(p);
                            }
                            Location newRanLoc = genRandomLoc(new Location(lastLoc.getWorld(), lastLoc.getX(), lastLoc.getY() + 1, lastLoc.getZ()));
                            int index = 0;
                            if (players.size() <= 15) {
                                index = players.indexOf(p);
                            }
                            p.playSound(newRanLoc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
                            newRanLoc.getBlock().setType(Material.STAINED_GLASS);
                            newRanLoc.getBlock().setData((byte) index);
                            dataValuesLoc.put(lastLoc, index);
                            newLoc.put(p, newRanLoc);
                            oldLoc.put(p, lastLoc);
                        }, 3);
                    }
                } catch (Exception e) {
                }
            }
        }, 20, 5);
    }

    private void registerEvents() {
        //getServer().getPluginManager().registerEvents(new InteractListener(), this);
        //getServer().getPluginManager().registerEvents(new JoinListener(), this);
    }

    private void registerCommands() {
        getCommand("dna").setExecutor(new DNACmd());
    }

    public static Location genRandomLoc(Location center) {
        Random rnd = new Random();
        int r = 3;
        if (rnd.nextBoolean()) {
            r = 4;
        }
        // given loc as the centre of the area you want, r as the max radius...
        double a = rnd.nextDouble() * 2 * Math.PI;
        double dist = rnd.nextDouble() * r;
        double y = 0;
        if (r == 4) {
            y = -1;
        }
        int add = 2;
        if (y == 0) {
            add = 1;
        }
        double x = Math.ceil(dist * Math.sin(a));
        double z = Math.ceil(dist * Math.cos(a));
        if (x >= -1.0 && x <= 1.0) {
            x = rnd.nextInt(3) + add;
            if (rnd.nextBoolean()) {
                x *= -1;
            }
        }

        if (z >= -1.0 && z <= 1.0) {
            z = rnd.nextInt(3) + add;
            if (rnd.nextBoolean()) {
                z *= -1;
            }
        }
        Location spawn = center.clone().add(x, y, z);
        return spawn;
    }

    public static double getDistance(Location l1, Location l2) {
        double x1 = l1.getX();
        double x2 = l2.getX();
        double z1 = l1.getZ();
        double z2 = l2.getZ();
        double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));
        return dist;
    }

    public static DNA getInstance() {
        return instance;
    }
}
