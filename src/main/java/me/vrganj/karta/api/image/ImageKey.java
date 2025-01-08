package me.vrganj.karta.api.image;

import java.util.UUID;

public record ImageKey(UUID ownerId, String image) {}
