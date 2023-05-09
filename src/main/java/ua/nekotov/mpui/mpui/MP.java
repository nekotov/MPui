package ua.nekotov.mpui.mpui;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;


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
        public float latitude;
        public float longitude;


        public String date;

        public String[] imageUrls;

        public int sellerId;
        public String sellerName;
        public boolean showWebsiteUrl;
        public int categoryId;

        public void Products(String itemId, String title, String description, int priceCents, String priceType, String cityName, String countryName, String countryAbbreviation, int distanceMeters, boolean onCountryLevel, boolean abroad, float latitude, float longitude, String date, String[] imageUrls, int sellerId, String sellerName, boolean showSoiUrl, boolean showWebsiteUrl, int categoryId, String categoryName) {
            this.itemId = itemId;
            this.title = title;
            this.description = description;
            this.priceCents = priceCents;
            this.priceType = priceType;
            this.cityName = cityName;
            this.latitude = latitude;
            this.longitude = longitude;
            this.date = date;
            this.imageUrls = imageUrls;
            this.sellerId = sellerId;
            this.sellerName = sellerName;
            this.showWebsiteUrl = showWebsiteUrl;
            this.categoryId = categoryId;
        }

        @Override
        public String toString() {
            Field fields[] = this.getClass().getDeclaredFields();
            StringBuilder sb = new StringBuilder();
            for (Field field : fields) {
                try {
                    if(field.getName().equals("imageUrls")) {
                        sb.append("imageUrls: " + Arrays.toString(this.imageUrls) + "\n");
                    } else {
                        try {
                            sb.append(field.getName() + ": " + field.get(this) + "\n");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

            }
            return sb.toString();

        }



        public ArrayList<String> getArray() {
            ArrayList<String> array = new ArrayList<String>();
            array.add(this.itemId);
            array.add(this.title);
            array.add(this.description);
            array.add(Integer.toString(this.priceCents));
            array.add(this.priceType);
            array.add(this.cityName);
            array.add(Float.toString(this.latitude));
            array.add(Float.toString(this.longitude));
            array.add(this.date);
            array.add(this.imageUrls.toString());
            array.add(Integer.toString(this.sellerId));
            array.add(this.sellerName);
            array.add(Boolean.toString(this.showWebsiteUrl));
            array.add(Integer.toString(this.categoryId));
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
                        add.showWebsiteUrl = sellerInformation.getBoolean("showWebsiteUrl");
                        add.categoryId = arr.getJSONObject(i).getInt("categoryId");
                        out.add(add);
                        System.out.println(add.title);

                    }
                    item_count += arr.length();
                }catch(Exception e){
                    return out;
                }

//                try {
//                    Thread.sleep(1234); // 1 sec speep
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

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

        public boolean isShowWebsiteUrl() {
            return showWebsiteUrl;
        }

        public void setShowWebsiteUrl(boolean showWebsiteUrl) {
            this.showWebsiteUrl = showWebsiteUrl;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
        // creae toJSON() method
        public String toJSON() {
            Field[] fields = this.getClass().getDeclaredFields();

            JSONObject obj = new JSONObject();
            for (Field field : fields) {
                try {
                    obj.put(field.getName(), field.get(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return obj.toString();
        }
    }
}