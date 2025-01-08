package me.vrganj.karta.api.panel;

import com.google.gson.*;
import me.vrganj.karta.api.image.ImageKey;
import me.vrganj.karta.api.panel.placement.PanelPlacement;

import java.lang.reflect.Type;
import java.util.UUID;

public class PanelAdapter implements JsonSerializer<Panel>, JsonDeserializer<Panel> {

    private final PanelFactory panelFactory;

    public PanelAdapter(PanelFactory panelFactory) {
        this.panelFactory = panelFactory;
    }

    @Override
    public Panel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        UUID ownerId = UUID.fromString(object.get("ownerId").getAsString());
        ImageKey imageKey = context.deserialize(object.get("imageKey"), ImageKey.class);
        PanelPlacement placement = context.deserialize(object.get("placement"), PanelPlacement.class);

        return panelFactory.createPanel(ownerId, imageKey, placement);
    }

    @Override
    public JsonElement serialize(Panel src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("ownerId", src.getOwnerId().toString());
        object.add("imageKey", context.serialize(src.getImageInput().imageKey()));
        object.add("placement", context.serialize(src.getPlacement()));

        return object;
    }
}
