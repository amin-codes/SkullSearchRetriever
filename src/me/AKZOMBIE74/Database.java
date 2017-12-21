package me.AKZOMBIE74;

/**
 * Created by AKZOMBIE74 on 12/21/2017.
 */
public enum Database {

    MINECRAFT_HEADS("https://minecraft-heads.com/", "index.php?option=com_k2&view=itemlist&task=search&searchword=",
                            "<!-- IMAGE START -->",
                            "<!-- IMAGE END -->",
                            "<!-- Pagination -->",
                            "https://www.w3.org/services/html2txt?url=",
                            "http://textures.minecraft.net/texture/",
                            "<font style=\"color:#666\">",
                            "</font>",
                            "<a href=\"/",
                            "\">"),
    HEADDB("http://headdb.com/view/all", //DB
                   "", //SEARCH
                   "<div class=\"card\">", //I_START
                   "</div><div class=\"col-xs-12 col-sm-6 col-md-4 col-lg-3 col-xl-3\" style=\"margin-bottom:20px;\">", //I_END
                   "<nav class=\"navbar navbar-light bg-faded\">", //PAGINATION
                   "", //TEXTURE_LINK_GETTER
                   "<a href=\"http://textures.minecraft.net/texture/", //TEXTURE_SPLITTER
                   "<div class=\"card-header\">", //NAME_START
                   "</div>", //NAME_END
                   "<a href=\"http://textures.minecraft.net/texture/", //URL_START
                   "\" download=" //URL_END
                   );
    public String DATABASE, SEARCH, START, END, END_SEARCH, TEXTURE_LINK_GETTER, TEXTURE_SPLITTER, NAME_START, NAME_END, URL_START, URL_END;

    private Database(String DATABASE, String SEARCH, String START, String END, String END_SEARCH,
                    String TEXTURE_LINK_GETTER, String TEXTURE_SPLITTER,
                    String NAME_START, String NAME_END,
                    String URL_START, String URL_END)
    {
        this.DATABASE = DATABASE;
        this.SEARCH = SEARCH;
        this.START = START;
        this.END = END;
        this.END_SEARCH = END_SEARCH;
        this.TEXTURE_LINK_GETTER = TEXTURE_LINK_GETTER;
        this.TEXTURE_SPLITTER = TEXTURE_SPLITTER;
        this.NAME_START = NAME_START;
        this.NAME_END = NAME_END;
        this.URL_START = URL_START;
        this.URL_END = URL_END;
    }

}
