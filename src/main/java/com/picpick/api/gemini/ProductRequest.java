package com.picpick.api.gemini;

public record ProductRequest(
                String productName,
                int martPrice,
                int onlinePrice) {
}