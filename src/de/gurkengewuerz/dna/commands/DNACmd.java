package de.gurkengewuerz.dna.commands;

import de.gurkengewuerz.dna.DNA;
import static de.gurkengewuerz.dna.DNA.genRandomLoc;
import static de.gurkengewuerz.dna.DNA.newLoc;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author gurkengewuerz.de
 */
public class DNACmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] args) {
        if (cs instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("dna")) {
                Player p = (Player) cs;
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("join")) {
                        if (DNA.players.contains(p)) {
                            DNA.players.remove(p);
                            p.sendMessage(DNA.prefix + "Du hast aufgehört DNA mitzuspielen!");
                        } else {
                            DNA.players.add(p);
                            Location newRanLoc = genRandomLoc(p.getLocation());
                            newRanLoc.getBlock().setType(Material.WOOL);
                            newLoc.put(p, newRanLoc);
                            p.sendMessage(DNA.prefix + "Du spielst nun DNA mit!");
                        }
                    }
                    if (args[0].equalsIgnoreCase("aufbauen")) {
                        final List<Location> loces = new ArrayList<>();
                        final List<Integer> intLocs = new ArrayList<>();
                        final int[] indexes = {0};
                        for (Map.Entry<Location, Integer> entry : DNA.dataValuesLoc.entrySet()) {
                            Location key = entry.getKey();
                            int value = entry.getValue();
                            loces.add(key);
                            intLocs.add(value);
                            indexes[0] = indexes[0] + 1;
                        }

                        indexes[0] = 0;
                        Bukkit.getScheduler().runTaskTimerAsynchronously(DNA.getInstance(), () -> {
                            Bukkit.getScheduler().runTask(DNA.getInstance(), () -> {
                                if (indexes[0] <= loces.size() - 1) {
                                    loces.get(indexes[0]).getBlock().setType(Material.STAINED_GLASS);
                                    loces.get(indexes[0]).getBlock().setData((byte) (int) intLocs.get(indexes[0]));
                                    loces.get(indexes[0]).getWorld().playSound(loces.get(indexes[0]), Sound.ENTITY_ITEM_PICKUP, 1, 2);
                                    indexes[0] = indexes[0] + 1;
                                }
                            });
                        }, 10, 2);
                    }
                }
            }
        }
        return true;
    }

}
