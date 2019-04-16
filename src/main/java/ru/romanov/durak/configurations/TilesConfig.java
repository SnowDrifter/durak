package ru.romanov.durak.configurations;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.request.Request;

import java.util.HashMap;
import java.util.Map;


public class TilesConfig implements DefinitionsFactory {

    private static final Map<String, Definition> TILES_DEFINITIONS = new HashMap<>();

    @Override
    public Definition getDefinition(String name, Request request) {
        return TILES_DEFINITIONS.get(name);
    }

    public static void addDefinitions() {
        addDefaultLayoutDefinition("home", "/WEB-INF/templates/home-body.jsp");
        addDefaultLayoutDefinition("singleplayer", "/WEB-INF/templates/singleplayer-body.jsp");
        addDefaultLayoutDefinition("multiplayer", "/WEB-INF/templates/multiplayer-body.jsp");
        addDefaultLayoutDefinition("login", "/WEB-INF/templates/login-body.jsp");
        addDefaultLayoutDefinition("registration", "/WEB-INF/templates/registration-body.jsp");
        addDefaultLayoutDefinition("rules", "/WEB-INF/templates/rules-body.jsp");
        addDefaultLayoutDefinition("statistics", "/WEB-INF/templates/statistics-body.jsp");
        addDefaultLayoutDefinition("accept", "/WEB-INF/templates/accept-body.jsp");
        addDefaultLayoutDefinition("access", "/WEB-INF/templates/access-body.jsp");
        addDefaultLayoutDefinition("edit-profile", "/WEB-INF/templates/edit-profile-body.jsp");
    }

    private static void addDefaultLayoutDefinition(String name, String body) {
        Attribute baseTemplate = new Attribute("/WEB-INF/templates/base-template.jsp");

        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("header", new Attribute("/WEB-INF/templates/header.jsp"));
        attributes.put("body", new Attribute(body));
        attributes.put("footer", new Attribute("/WEB-INF/templates/footer.jsp"));

        TILES_DEFINITIONS.put(name, new Definition(name, baseTemplate, attributes));
    }
}
