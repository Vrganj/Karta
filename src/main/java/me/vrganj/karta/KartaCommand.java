package me.vrganj.karta;

import me.vrganj.karta.image.ImageKey;
import me.vrganj.karta.image.source.ImageSource;
import me.vrganj.karta.panel.placement.PanelDimensions;
import me.vrganj.karta.panel.placement.PanelPlacement;
import me.vrganj.karta.panel.placement.PanelRotation;
import me.vrganj.karta.util.BukkitUtil;
import me.vrganj.karta.util.Util;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class KartaCommand implements TabExecutor {
    private static final double RAY_TRACE_DISTANCE = 10;

    private final Logger logger;
    private final PanelManager panelManager;
    private final ImageSource imageSource;

    public KartaCommand(Logger logger, PanelManager panelManager, ImageSource imageSource) {
        this.logger = logger;
        this.panelManager = panelManager;
        this.imageSource = imageSource;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length < 2) {
                Util.send(player, "<red>/karta place <image>");
                return true;
            }

            var imageKey = new ImageKey(player.getUniqueId(), args[1]);

            // TODO: async
            if (!imageSource.exists(imageKey).join()) {
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

            var location = block.getRelative(face).getLocation();
            // TODO: calculate instead of hardcoding
            var dimensions = new PanelDimensions(5, 5);
            var placement = new PanelPlacement(
                    BukkitUtil.toPanelLocation(location),
                    BukkitUtil.toPanelFace(face),
                    PanelRotation.NONE,
                    dimensions
            );
            panelManager.addDefaultPanel(placement, imageKey);

            Util.send(player, "<white>Placed <green><image>", Placeholder.unparsed("image", imageKey.image()));
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
