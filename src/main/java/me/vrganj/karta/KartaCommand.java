package me.vrganj.karta;

import me.vrganj.karta.renderer.ImageRenderer;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public class KartaCommand implements TabExecutor {
    private static final double RAY_TRACE_DISTANCE = 10;

    private final Karta plugin;

    public KartaCommand(Karta plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length < 2) {
                Util.send(player, "<red>/karta place <image>");
                return true;
            }

            var file = new File(plugin.getDataFolder(), args[1]);

            if (!file.exists()) {
                Util.send(player, "<red>That image doesn't exist!");
                return true;
            }

            var trace = player.rayTraceBlocks(RAY_TRACE_DISTANCE);

            if (trace == null || trace.getHitBlock() == null || trace.getHitBlockFace() == null) {
                Util.send(player, "<red>No target block!");
                return true;
            }

            var face = trace.getHitBlockFace();
            var block = trace.getHitBlock();

            BlockFace down, right;

            if (face == BlockFace.UP || face == BlockFace.DOWN) {
                var direction = Util.getDirection(player.getLocation().getYaw());

                right = Util.getRight(direction);
                down = direction.getOppositeFace();
            } else {
                down = BlockFace.DOWN;
                right = Util.getRight(face.getOppositeFace());
            }

            var renderer = new ImageRenderer(file);
            var location = block.getRelative(face).getLocation();
            var panel = new Panel(location, renderer, down, right, face);

            plugin.getPanelManager().addPanel(panel);
            Util.send(player, "<white>Placed <green><image>", Placeholder.unparsed("image", file.getName()));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("place").filter(subcommand -> subcommand.startsWith(args[0].toLowerCase())).toList();
        }

        return null;
    }
}
