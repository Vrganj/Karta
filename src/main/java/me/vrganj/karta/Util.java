package me.vrganj.karta;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Util {
    private static final BlockFace[] DIRECTIONS = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };

    public static BlockFace getDirection(float yaw) {
        return DIRECTIONS[Math.round(yaw / 90f) & 0x3];
    }

    public static Direction toDirection(BlockFace face) {
        return Direction.getNearest(face.getModX(), face.getModY(), face.getModZ());
    }

    public static BlockFace getRight(BlockFace face) {
        return BlockFace.values()[(face.ordinal() + 1) % 4];
    }

    public static void spawnMap(Location location, Player player, int map, byte[] data, int width, int height, Direction direction, int rotation) {
        var world = ((CraftWorld) player.getWorld()).getHandle();
        var entity = new ItemFrame(world, new BlockPos(location.getX(), location.getY(), location.getZ()), direction);
        var item = new ItemStack(Items.FILLED_MAP);
        item.getOrCreateTag().putInt("map", map);
        entity.setItem(item, true, false);
        entity.setRotation(rotation);
        entity.setId(map);
        //entity.setInvisible(true);

        var connection = ((CraftPlayer) player).getHandle().connection;
        connection.send(entity.getAddEntityPacket());
        connection.send(new ClientboundSetEntityDataPacket(entity.getId(), entity.getEntityData(), true));
        connection.send(new ClientboundMapItemDataPacket(map, (byte) 0, false, null, new MapItemSavedData.MapPatch(0, 0, width, height, data)));
    }

    public static void send(CommandSender target, String text, TagResolver... tagResolvers) {
        target.sendMessage(Karta.PREFIX.append(MiniMessage.miniMessage().deserialize(text, tagResolvers)));
    }

    public static int getDistance(Chunk a, Chunk b) {
        return Math.max(Math.abs(b.getX() - a.getX()), Math.abs(b.getZ() - a.getZ()));
    }
}
