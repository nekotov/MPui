package ua.nekotov.mpui.mpui;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class MP {
    // create function that returns string array
    public static class Suggestions {
        public String completion;
        public int categoryId;
        public String categoryName;
        public int parentId;
        public String parentName;

        public Suggestions(String completion, int categoryId, String categoryName, int parentId, String parentName) {
            this.completion = completion;
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.parentId = parentId;
            this.parentName = parentName;
        }

        public ArrayList<String> getArray() {
            ArrayList<String> array = new ArrayList<String>();
            array.add(this.completion);
            array.add(Integer.toString(this.categoryId));
            array.add(this.categoryName);
            array.add(Integer.toString(this.parentId));
            array.add(this.parentName);
            return array;
        }

        public static ArrayList<Suggestions> get(String args) throws IOException {
            URL url = new URL("https://www.marktplaats.nl/header/searches/suggestions?prefix=" + URLEncoder.encode(args, StandardCharsets.UTF_8) + "&category=0");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setDoOutput(true);

            ArrayList<Suggestions> out = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }


                JSONObject obj = new JSONObject(response.toString());
                JSONArray arr = obj.getJSONArray("completions");
                for (int i = 0; i < arr.length(); i++) {
                    var add = new Suggestions(arr.getJSONObject(i).getString("completion"), arr.getJSONObject(i).getInt("categoryId"), arr.getJSONObject(i).getString("categoryName"), arr.getJSONObject(i).getInt("parentId"), arr.getJSONObject(i).getString("parentName"));
                    out.add(add);

                }

            }

            return out;
        }

    }

    public static class Products {
        public String itemId;
        public String title;
        public String description;

        public int priceCents;
        public String priceType;


        public String cityName;
        public String countryName;
        public String countryAbbreviation;
        public int distanceMeters;
        public boolean isBuyerLocation;
        public boolean onCountryLevel;
        public boolean abroad;
        public float latitude;
        public float longitude;


        public String date;

        public String[] imageUrls;

        public int sellerId;
        public String sellerName;
        public boolean showSoiUrl;
        public boolean showWebsiteUrl;
        public boolean isVerified;


        public int categoryId;
        public String categoryName;

        public void Products(String itemId, String title, String description, int priceCents, String priceType, String cityName, String countryName, String countryAbbreviation, int distanceMeters, boolean isBuyerLocation, boolean onCountryLevel, boolean abroad, float latitude, float longitude, String date, String[] imageUrls, int sellerId, String sellerName, boolean showSoiUrl, boolean showWebsiteUrl, boolean isVerified, int categoryId, String categoryName) {
            this.itemId = itemId;
            this.title = title;
            this.description = description;
            this.priceCents = priceCents;
            this.priceType = priceType;
            this.cityName = cityName;
            this.countryName = countryName;
            this.countryAbbreviation = countryAbbreviation;
            this.distanceMeters = distanceMeters;
            this.isBuyerLocation = isBuyerLocation;
            this.onCountryLevel = onCountryLevel;
            this.abroad = abroad;
            this.latitude = latitude;
            this.longitude = longitude;
            this.date = date;
            this.imageUrls = imageUrls;
            this.sellerId = sellerId;
            this.sellerName = sellerName;
            this.showSoiUrl = showSoiUrl;
            this.showWebsiteUrl = showWebsiteUrl;
            this.isVerified = isVerified;
            this.categoryId = categoryId;
            this.categoryName = categoryName;
        }

        @Override
        public String toString() {
            return this.itemId + "\t" + this.title + "\t" + this.description + "\t" + this.priceCents + "\t" + this.priceType + "\t" + this.cityName + "\t" + this.countryName + "\t" + this.countryAbbreviation + "\t" + this.distanceMeters + "\t" + this.isBuyerLocation + "\t" + this.onCountryLevel + "\t" + this.abroad + "\t" + this.latitude + "\t" + this.longitude + "\t" + this.date + "\t" + this.imageUrls + "\t" + this.sellerId + "\t" + this.sellerName + "\t" + this.showSoiUrl + "\t" + this.showWebsiteUrl + "\t" + this.isVerified + "\t" + this.categoryId + "\t" + this.categoryName;
        }

        public ArrayList<String> getArray() {
            ArrayList<String> array = new ArrayList<String>();
            array.add(this.itemId);
            array.add(this.title);
            array.add(this.description);
            array.add(Integer.toString(this.priceCents));
            array.add(this.priceType);
            array.add(this.cityName);
            array.add(this.countryName);
            array.add(this.countryAbbreviation);
            array.add(Integer.toString(this.distanceMeters));
            array.add(Boolean.toString(this.isBuyerLocation));
            array.add(Boolean.toString(this.onCountryLevel));
            array.add(Boolean.toString(this.abroad));
            array.add(Float.toString(this.latitude));
            array.add(Float.toString(this.longitude));
            array.add(this.date);
            array.add(this.imageUrls.toString());
            array.add(Integer.toString(this.sellerId));
            array.add(this.sellerName);
            array.add(Boolean.toString(this.showSoiUrl));
            array.add(Boolean.toString(this.showWebsiteUrl));
            array.add(Boolean.toString(this.isVerified));
            array.add(Integer.toString(this.categoryId));
            array.add(this.categoryName);
            return array;
        }


        public static ArrayList<Products> get(String args, int start, int pages) throws IOException {

            int item_count = start;
            ArrayList<Products> out = new ArrayList<>();

            do {
                URL url = new URL("https://www.marktplaats.nl/lrp/api/search?limit=100&offset=" + item_count + "&query=" + URLEncoder.encode(args, StandardCharsets.UTF_8) + "&searchInTitleAndDescription=true&viewOptions=list-view");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                con.setDoOutput(true);


                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }


                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray arr = obj.getJSONArray("listings");
                    for (int i = 0; i < arr.length(); i++) {
                        var add = new Products();
                        add.itemId = arr.getJSONObject(i).getString("itemId");
                        add.title = arr.getJSONObject(i).getString("title");
                        add.description = arr.getJSONObject(i).getString("description");
                        JSONObject priceInfo = arr.getJSONObject(i).getJSONObject("priceInfo");
                        add.priceCents = priceInfo.getInt("priceCents");
                        add.priceType = priceInfo.getString("priceType");
                        JSONObject location = arr.getJSONObject(i).getJSONObject("location");
                        if (location.has("cityName"))
                            add.cityName = location.getString("cityName");
                        if (location.has("countryName"))
                            add.countryName = location.getString("countryName");
                        if (location.has("countryAbbreviation"))
                            add.countryAbbreviation = location.getString("countryAbbreviation");
                        add.distanceMeters = location.getInt("distanceMeters");
                        add.isBuyerLocation = location.getBoolean("isBuyerLocation");
                        add.onCountryLevel = location.getBoolean("onCountryLevel");
                        add.abroad = location.getBoolean("abroad");
                        add.latitude = location.getFloat("latitude");
                        add.longitude = location.getFloat("longitude");

                        add.date = arr.getJSONObject(i).getString("date");


                        if (arr.getJSONObject(i).has("imageUrls")) {
                            JSONArray arr2 = arr.getJSONObject(i).getJSONArray("imageUrls");
                            add.imageUrls = new String[arr2.length()];
                            for (int j = 0; j < arr2.length(); j++) {
                                add.imageUrls[j] = arr2.getString(j);
                            }
                        }
                        JSONObject sellerInformation = arr.getJSONObject(i).getJSONObject("sellerInformation");
                        add.sellerId = sellerInformation.getInt("sellerId");
                        add.sellerName = sellerInformation.getString("sellerName");
                        add.showSoiUrl = sellerInformation.getBoolean("showSoiUrl");
                        add.showWebsiteUrl = sellerInformation.getBoolean("showWebsiteUrl");
                        if(sellerInformation.has("isVerified"))
                            add.isVerified = sellerInformation.getBoolean("isVerified");
                        add.categoryId = arr.getJSONObject(i).getInt("categoryId");
                        if (arr.getJSONObject(i).has("categoryName"))
                            add.categoryName = arr.getJSONObject(i).getString("categoryName");
                        out.add(add);
                        System.out.println(add.title);

                    }
                    item_count += arr.length();
                }

            } while ((item_count % 100 == 0) && ((item_count - start) < (pages * 100)));

            return out;

        }

        public static ArrayList<Products> get(String args) throws IOException {
            return get(args, 0, 1);
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getPriceCents() {
            return priceCents;
        }

        public void setPriceCents(int priceCents) {
            this.priceCents = priceCents;
        }

        public String getPriceType() {
            return priceType;
        }

        public void setPriceType(String priceType) {
            this.priceType = priceType;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryAbbreviation() {
            return countryAbbreviation;
        }

        public void setCountryAbbreviation(String countryAbbreviation) {
            this.countryAbbreviation = countryAbbreviation;
        }

        public int getDistanceMeters() {
            return distanceMeters;
        }

        public void setDistanceMeters(int distanceMeters) {
            this.distanceMeters = distanceMeters;
        }

        public boolean isBuyerLocation() {
            return isBuyerLocation;
        }

        public void setBuyerLocation(boolean buyerLocation) {
            isBuyerLocation = buyerLocation;
        }

        public boolean isOnCountryLevel() {
            return onCountryLevel;
        }

        public void setOnCountryLevel(boolean onCountryLevel) {
            this.onCountryLevel = onCountryLevel;
        }

        public boolean isAbroad() {
            return abroad;
        }

        public void setAbroad(boolean abroad) {
            this.abroad = abroad;
        }

        public float getLatitude() {
            return latitude;
        }

        public void setLatitude(float latitude) {
            this.latitude = latitude;
        }

        public float getLongitude() {
            return longitude;
        }

        public void setLongitude(float longitude) {
            this.longitude = longitude;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String[] getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(String[] imageUrls) {
            this.imageUrls = imageUrls;
        }

        public int getSellerId() {
            return sellerId;
        }

        public void setSellerId(int sellerId) {
            this.sellerId = sellerId;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public boolean isShowSoiUrl() {
            return showSoiUrl;
        }

        public void setShowSoiUrl(boolean showSoiUrl) {
            this.showSoiUrl = showSoiUrl;
        }

        public boolean isShowWebsiteUrl() {
            return showWebsiteUrl;
        }

        public void setShowWebsiteUrl(boolean showWebsiteUrl) {
            this.showWebsiteUrl = showWebsiteUrl;
        }

        public boolean isVerified() {
            return isVerified;
        }

        public void setVerified(boolean verified) {
            isVerified = verified;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}