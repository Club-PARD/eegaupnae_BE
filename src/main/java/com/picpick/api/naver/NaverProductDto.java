package com.picpick.api.naver;

import lombok.*;
import org.json.JSONObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverProductDto {

    private String productId;
    private String productBrand;
    private String productName;
    private String link;
    private String image;
    private Integer lowestPrice;

    public NaverProductDto(JSONObject itemJson) {
        // Naver returns 'title', 'brand', and 'lprice'
        // Using optString to prevent JSONException if a field is missing
        this.productId = itemJson.optString("productId", "");
        this.productBrand = itemJson.optString("brand", "");

        // Naver wraps search terms in <b> tags; we strip them for the DB
        String rawTitle = itemJson.optString("title", "");
        this.productName = rawTitle.replaceAll("<[^>]*>", "");

        this.link = itemJson.optString("link", "");
        this.image = itemJson.optString("image", "");

        // lprice is a String in the JSON, we parse to Integer
        String priceStr = itemJson.optString("lprice", "0");
        try {
            this.lowestPrice = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            this.lowestPrice = 0;
        }
    }
}