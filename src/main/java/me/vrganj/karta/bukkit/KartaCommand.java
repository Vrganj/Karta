package me.vrganj.karta.bukkit;

import me.vrganj.karta.api.PanelManager;
import me.vrganj.karta.api.image.ImageKey;
import me.vrganj.karta.api.image.source.ImageSource;
import me.vrganj.karta.api.panel.placement.PanelDimensions;
import me.vrganj.karta.api.panel.placement.PanelPlacement;
import me.vrganj.karta.api.panel.placement.PanelRotation;
import me.vrganj.karta.bukkit.util.BukkitUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed;

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
            if (args.length < 1) {
                sender.sendRichMessage("<red>/karta <...>");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "download" -> {
                    // TODO: sanitize

                    if (!sender.hasPermission("karta.download")) {
                        sender.sendRichMessage("<red>Insufficient permissions!");
                        return true;
                    }

                    var name = args[1];
                    var imageKey = new ImageKey(player.getUniqueId(), name);
                    var file = imageSource.getFile(imageKey);

                    if (file.getParentFile().mkdirs()) {
                        sender.sendRichMessage("<green>Created " + file.getName() + " at " + file.getParentFile().getAbsolutePath());
                    } else {
                        sender.sendRichMessage("<red>Couldn't create " + file.getName() + " at " + file.getParentFile().getAbsolutePath());
                    }

                    try (var input = new BufferedInputStream(new URL(args[2]).openStream());
                         var output = new FileOutputStream(file)) {
                        input.transferTo(output);
                    } catch (IOException e) {
                        sender.sendRichMessage("<red>Something went wrong");
                        logger.warn("Failed to download image", e);
                    }
                }

                case "place" -> {
                    if (!sender.hasPermission("karta.place")) {
                        sender.sendRichMessage("<red>Insufficient permissions!");
                        return true;
                    }

                    if (args.length < 2) {
                        sender.sendRichMessage("<red>You didn't specify the image name");
                        return true;
                    }

                    var imageKey = new ImageKey(player.getUniqueId(), args[1]);

                    if (!imageSource.exists(imageKey).join()) {
                        sender.sendRichMessage("<red>That image doesn't exist!");
                        return true;
                    }

                    var trace = player.rayTraceBlocks(RAY_TRACE_DISTANCE);

                    if (trace == null || trace.getHitBlock() == null || trace.getHitBlockFace() == null) {
                        player.sendRichMessage("<red>No target block!");
                        return true;
                    }

                    var face = trace.getHitBlockFace();
                    var block = trace.getHitBlock();

                    var location = block.getRelative(face).getLocation();
                    // TODO: calculate instead of hardcoding

                    if (args.length < 4) {
                        sender.sendRichMessage("<red>You didn't specify the width and height");
                        return true;
                    }

                    int width = Integer.parseInt(args[2]);
                    int height = Integer.parseInt(args[3]);

                    var dimensions = new PanelDimensions(width, height);

                    var placement = new PanelPlacement(
                            BukkitUtil.toPanelLocation(location),
                            BukkitUtil.toPanelFace(face),
                            PanelRotation.NONE,
                            dimensions
                    );

                    panelManager.addDefaultPanel(player.getUniqueId(), imageKey, placement);

                    sender.sendRichMessage("<white>Placed <green><image>", unparsed("image", imageKey.image()));
                }

                case "delete" -> {
                    if (!sender.hasPermission("karta.delete")) {
                        sender.sendRichMessage("<red>Insufficient permissions!");
                        return true;
                    }

                    var imageKey = new ImageKey(player.getUniqueId(), args[1]);
                    imageSource.deleteImage(imageKey);
                    sender.sendRichMessage("<green>Image deleted");
                }

                default -> {
                    sender.sendRichMessage("<red>Unknown subcommand");
                }
            }
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
