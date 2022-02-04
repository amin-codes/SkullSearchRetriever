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
                            "\">",
            false,
            "GET"),
    HEADDB("https://headdb.org/api/category/all", //DB
                   "", //SEARCH
                   "", //I_START
                   "", //I_END
                   "", //PAGINATION
                   "", //TEXTURE_LINK_GETTER
                   "", //TEXTURE_SPLITTER
                   "*|name", //NAME_START
                   "", //NAME_END
                   "*|valueDecoded|textures|SKIN|url", //URL_START
                   "", //URL_END
                   true,
            "GET"),
    MINESKIN("https://api.mineskin.org/",
            "get/list/1?size=10&filter=",
            "",
            "",
            "",
            "",
            "", //7
            "skins|*|name",
            "",
            "skins|*|url",
            "",
            true,
            "GET"
    ),
    FRESHCOAL("https://freshcoal.com/php_includes/search_query.php?",
            "search=",
            "",
            "",
            "",
            "",
            "",
            "*|HeadName", //NAME_START
            "",
            "*|Url", //URL_START
            "",
            true,
            "POST");

    public String DATABASE, SEARCH, START, END, END_SEARCH, TEXTURE_LINK_GETTER, TEXTURE_SPLITTER, NAME_START, NAME_END, URL_START, URL_END, REQUEST_METHOD;
    public boolean USE_JSON;
    private Database(String DATABASE, String SEARCH, String START, String END, String END_SEARCH,
                    String TEXTURE_LINK_GETTER, String TEXTURE_SPLITTER,
                    String NAME_START, String NAME_END,
                    String URL_START, String URL_END, boolean use_json, String request)
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
        this.USE_JSON = use_json;
        this.REQUEST_METHOD = request;
    }

}
