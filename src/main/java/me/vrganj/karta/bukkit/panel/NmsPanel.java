package me.vrganj.karta.bukkit.panel;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.vrganj.karta.api.image.ImageData;
import me.vrganj.karta.api.image.ImageInput;
import me.vrganj.karta.api.panel.Panel;
import me.vrganj.karta.api.panel.placement.PanelPlacement;
import me.vrganj.karta.bukkit.util.NmsUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class NmsPanel implements Panel {

    private final Logger logger;

    private final Object2ObjectMap<UUID, IntList> showing = new Object2ObjectOpenHashMap<>();

    private final UUID ownerId;
    private final ImageInput imageInput;
    private final PanelPlacement placement;

    public NmsPanel(Logger logger, UUID ownerId, ImageInput imageInput, PanelPlacement placement) {
        this.logger = logger;

        this.ownerId = ownerId;
        this.imageInput = imageInput;
        this.placement = placement;
    }

    @Override
    public UUID getOwnerId() {
        return ownerId;
    }

    @Override
    public PanelPlacement getPlacement() {
        return placement;
    }

    @Override
    public ImageInput getImageInput() {
        return imageInput;
    }

    @Override
    public CompletableFuture<Void> show(UUID playerId) {
        var player = Bukkit.getPlayer(playerId);

        if (player == null) {
            return CompletableFuture.completedFuture(null);
        }

        var connection = ((CraftPlayer) player).getHandle().connection;
        var dimensions = placement.dimensions();
        var entities = new IntArrayList(dimensions.width() * dimensions.height());

        Direction down = NmsUtil.fromPanelFace(placement.down());
        Direction right = NmsUtil.fromPanelFace(placement.right());

        for (int row = 0; row < dimensions.height(); row++) {
            for (int col = 0; col < dimensions.width(); col++) {
                var world = Bukkit.getWorld(placement.location().world());
                var itemFrame = new ItemFrame(EntityType.GLOW_ITEM_FRAME, ((CraftWorld) world).getHandle());
                var itemStack = new ItemStack(Items.FILLED_MAP);
                itemStack.set(DataComponents.MAP_ID, new MapId(-itemFrame.getId()));
                itemFrame.setItem(itemStack, false, false);

                itemFrame.setDirection(NmsUtil.fromPanelFace(placement.face()));
                itemFrame.rotate(NmsUtil.fromPanelRotation(placement.rotation()));

                itemFrame.setPos(
                        placement.location().x() + row * down.getStepX() + col * right.getStepX(),
                        placement.location().y() + row * down.getStepY() + col * right.getStepY(),
                        placement.location().z() + row * down.getStepZ() + col * right.getStepZ()
                );

                // send item frame entity
                connection.send(itemFrame.getAddEntityPacket(null));

                // send item frame contents
                connection.send(new ClientboundSetEntityDataPacket(itemFrame.getId(), itemFrame.getEntityData().packAll()));

                entities.add(itemFrame.getId());
            }
        }

        showing.put(player.getUniqueId(), entities);

        return CompletableFuture.runAsync(() -> {
            ImageData data;

            try {
                data = imageInput.render().join();
            } catch (CompletionException e) {
                logger.warn("Failed to render image {}", imageInput, e.getCause());
                throw e;
            }

            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (int row = 0; row < dimensions.height(); row++) {
                for (int col = 0; col < dimensions.width(); col++) {
                    int index = row * dimensions.width() + col;
                    int entityId = entities.getInt(index);
                    var mapId = new MapId(-entityId);

                    byte[] segment = new byte[128*128];

                    for (int i = 0; i < 128; i++) {
                        System.arraycopy(data.data(), (i + 128*row) * data.width() + 128 * col, segment, 128*i, 128);
                    }

                    var patch = new MapItemSavedData.MapPatch(0, 0, 128, 128, segment);
                    var packet = new ClientboundMapItemDataPacket(mapId, (byte) 0, false, null, patch);

                    // TODO: bundle?
                    var future = new CompletableFuture<Void>();
                    futures.add(future);

                    connection.send(packet, new PacketSendListener() {
                        @Override
                        public void onSuccess() {
                            future.complete(null);
                        }

                        @Override
                        public @Nullable Packet<?> onFailure() {
                            future.complete(null);
                            return PacketSendListener.super.onFailure();
                        }
                    });
                }
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        });
    }

    @Override
    public void hide(UUID playerId) {
        var player = Bukkit.getPlayer(playerId);

        if (player == null) {
            return;
        }

        // TODO: check if this is called on quit

        var ids = showing.remove(player.getUniqueId());

        if (ids == null) {
            return;
        }

        var packet = new ClientboundRemoveEntitiesPacket(ids);
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

    public Map<UUID, IntList> getShowing() {
        return showing;
    }
}
