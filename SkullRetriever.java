
//....imports here

/**
 * Created by AKZOMBIE74 on 12/20/2017.
 */
public class SkullRetriever {

    private static final String DATABASE = "https://minecraft-heads.com/";
    private static final String DATABASE_SEARCH = DATABASE + "index.php?option=com_k2&view=itemlist&task=search&searchword=";
    private static final String STARTER = "<!-- IMAGE START -->";
    private static final String END = "<!-- IMAGE END -->";
    private static final String END_SEARCH = "<!-- Pagination -->";

    private static final String TEXTURE_LINK_GETTER = "https://www.w3.org/services/html2txt?url=";
    private static final String TEXTURE_SPLITTER = "http://textures.minecraft.net/texture/";

    /**
     * Returns the texture link for skull url.
     * @param u The url from minecraft-heads.com/ that leads to the skull.
     * @return The textures.minecraft.net/texture/ link to your skull.
     */
    public static String getTextureFromURL(String u)
    {
        String texture = "";
        try {
            URL url = new URL(TEXTURE_LINK_GETTER+u);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;

            while ( (line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.contains(TEXTURE_SPLITTER))
                {
                    texture = TEXTURE_SPLITTER + line.split(TEXTURE_SPLITTER)[1];
                    break;
                }
            }

            br.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return texture;
    }

    /**
     * This HashMap contains the options returned by minecraft-heads.com/
     * @return Returns a HashMap of <Skin name, skin url> for search query.
     * @param entity_name The search query
     */
    public static HashMap<String, String> getSkullOptions(String entity_name) {
        boolean start_recording = false;
        HashMap<String, String> namesAndURLS = new HashMap<>();
        try {
            URL url = new URL(DATABASE_SEARCH + entity_name.replace(" ", "%20"));
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            String skinName = "";
            String skinURL = "";
            while ( (line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.equals(END_SEARCH)) break;
                if (start_recording)
                {
                    if (line.startsWith("<font style=\"color:#666\">")) //Get name
                    {
                        skinName = line.replace("<font style=\"color:#666\">", "").replace("</font>", "");
                        continue;
                    }
                    else if (line.startsWith("<a href=\"/")) //Get url
                    {
                        skinURL = line.replace("<a href=\"/", "").replace("\">", "");
                        continue;
                    }
                }
                if (line.equals(STARTER) || line.equals(END)) //Get next option
                {
                    if (start_recording) namesAndURLS.put(skinName.toLowerCase(), DATABASE + skinURL);
                    start_recording = !start_recording;
                }
            }
            br.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return namesAndURLS;
    }

    /*
     * @return skull texture of the skull with the name that matches the search
     * @param search The skull to search for.
     */
    public static String getMostRelevantSkull(String search)
    {
        search = search.toLowerCase();

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
    public static ItemStack getCustomSkull(String url, String name) {
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
