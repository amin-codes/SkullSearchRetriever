package me.AKZOMBIE74;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by AKZOMBIE74 on 12/20/2017.
 */
public class SkullRetriever {

    private Database db;

    public SkullRetriever()
    {
        db = Database.MINECRAFT_HEADS;
    }

    public SkullRetriever(Database d)
    {
        db = d;
    }

    private Database getDB()
    {
        return db;
    }

    public void setDatabase(Database d)
    {
        db = d;
    }

    /**
     * Returns the texture link for skull url.
     * @param u The url from minecraft-heads.com/ that leads to the skull.
     * @return The textures.minecraft.net/texture/ link to your skull.
     */
    public String getTextureFromURL(String u)
    {
        String texture = "";
        if (!getDB().TEXTURE_LINK_GETTER.equals("")) {
            try {
                URL url = new URL(getDB().TEXTURE_LINK_GETTER + u);
                URLConnection is = url.openConnection();

                is.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                is.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(is.getInputStream()));

                String line;

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.contains(getDB().TEXTURE_SPLITTER)) {
                        texture = getDB().TEXTURE_SPLITTER + line.split(getDB().TEXTURE_SPLITTER)[1];
                        break;
                    }
                }

                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            texture = u;
        }
        return texture;
    }

    /**
     * This HashMap contains the options returned by minecraft-heads.com/
     * @return Returns a HashMap of <Skin name, skin url> for search query.
     * @param entity_name The search query
     */
    public HashMap<String, String> getSkullOptions(String entity_name) {
        boolean start_recording = false;
        boolean getting_name = false;
        HashMap<String, String> namesAndURLS = new HashMap<>();
        try {
            String search = getDB().SEARCH.equals("") ? getDB().DATABASE : getDB().DATABASE + getDB().SEARCH + entity_name.replace(" ", "%20");
            URL url = new URL(search);
            URLConnection is = url.openConnection();

            is.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            is.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(is.getInputStream()));
            String line;
            String skinName = "";
            String skinURL = "";
            while ( (line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.equals(getDB().END_SEARCH)) break;
                if (start_recording)
                {
                    if (getting_name)
                    {
                        skinName = line;
                        getting_name = false;
                        continue;
                    }

                    if (line.startsWith(getDB().NAME_START)) //Get name
                    {
                        if (getDB().TEXTURE_LINK_GETTER.equals("")) {
                            getting_name = true;
                            continue;
                        }
                        skinName = line.substring(line.indexOf(getDB().NAME_START) + getDB().NAME_START.length(), line.indexOf(getDB().NAME_END));
                        continue;
                    }
                    else if (line.startsWith(getDB().URL_START)) //Get url
                    {
                        skinURL = line.substring(line.indexOf(getDB().URL_START) + getDB().URL_START.length(), line.indexOf(getDB().URL_END));
                        skinURL = getDB().TEXTURE_LINK_GETTER.equals("") ? "http://textures.minecraft.net/texture/"+skinURL : getDB().DATABASE + skinURL;
                        continue;
                    }
                }

                //Get next option
                if (line.equals(getDB().START) && !start_recording) start_recording = true;
                else if (line.equals(getDB().END) && start_recording)
                {
                    namesAndURLS.put(skinName, skinURL.trim());
                    start_recording = false;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return namesAndURLS;
    }

    /*
     * @return skull texture of the skull with the name that matches the search
     * @param search The skull to search for.
     */
    public String getMostRelevantSkull(String search)
    {
        HashMap<String, String> options = getSkullOptions(search);
        String texture = getTextureFromURL(options.get(options.keySet().stream().filter(search::equalsIgnoreCase).collect(Collectors.toList()).get(0)));

        return texture;
    }

    /*
       @author TheLexoPlexx with modifications by AKZOMBIE74
       @param url The texture url.
       @param name The display name for the ItemStack.
       @return A skull ItemStack with the given texture.
     */
    public ItemStack getCustomSkull(String url, String name) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        propertyMap.put("textures", new Property("textures", new String(encodedData)));
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        try {
            getField(headMetaClass, "profile", GameProfile.class, 0).set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    /*
       @author TheLexoPlexx
     */
    private static <T> Field getField(Class<?> target, String name, Class<T> fieldType, int index) {
        for (final Field field : target.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);
                return field;
            }
        }

        // Search in parent classes
        if (target.getSuperclass() != null)
            return getField(target.getSuperclass(), name, fieldType, index);
        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }

}

