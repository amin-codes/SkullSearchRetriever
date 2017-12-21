package me.AKZOMBIE74;

import org.bukkit.plugin.java.JavaPlugin;



/**
 * Created by AKZOMBIE74 on 12/21/2017.
 */
public class SkullSearch extends JavaPlugin{
    private SkullRetriever skullRetriever;

    private static final Object lock = new Object(); // little allocated
    private static SkullSearch instance;


    @Override
    public void onEnable()
    {
        instance = this;
        skullRetriever = new SkullRetriever(); //Default database is minecraft-heads.com
    }

    @Override
    public void onDisable()
    {
        skullRetriever = null;
        instance = null;
    }

    /*
     * @return The SkullRetriever class for getting skulls via search
     */
    public SkullRetriever getAPI()
    {
        return skullRetriever;
    }

    /*
     * Only way to access the api
     */
    public static SkullSearch getInstance() {
        SkullSearch inst = instance;
        if (inst == null) {
            synchronized(lock) {
                if (inst == null) {
                    inst = instance = SkullSearch.getPlugin(SkullSearch.class);
                }
            }
        }
        return inst;
    }
}
