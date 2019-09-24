package ru.romanov.durak.config;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.request.Request;

import java.util.HashMap;
import java.util.Map;


public class TilesDefinitionsFactory implements DefinitionsFactory {

    private static final Map<String, Definition> TILES_DEFINITIONS = new HashMap<>();
    private static final String TEMPLATE_FOLDER = "/WEB-INF/templates/";

    static {
        addDefaultLayoutDefinition("home", "home-body.jsp");
        addDefaultLayoutDefinition("singleplayer", "singleplayer-body.jsp");
        addDefaultLayoutDefinition("multiplayer", "multiplayer-body.jsp");
        addDefaultLayoutDefinition("login", "login-body.jsp");
        addDefaultLayoutDefinition("registration", "registration-body.jsp");
        addDefaultLayoutDefinition("rules", "rules-body.jsp");
        addDefaultLayoutDefinition("statistics", "statistics-body.jsp");
        addDefaultLayoutDefinition("accept", "accept-body.jsp");
        addDefaultLayoutDefinition("access", "access-body.jsp");
        addDefaultLayoutDefinition("edit-profile", "edit-profile-body.jsp");
    }

    @Override
    public Definition getDefinition(String name, Request request) {
        return TILES_DEFINITIONS.get(name);
    }

    private static void addDefaultLayoutDefinition(String name, String body) {
        Attribute baseTemplate = new Attribute(TEMPLATE_FOLDER + "base-template.jsp");

        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("header", new Attribute(TEMPLATE_FOLDER + "header.jsp"));
        attributes.put("body", new Attribute(TEMPLATE_FOLDER + body));
        attributes.put("footer", new Attribute(TEMPLATE_FOLDER + "footer.jsp"));

        TILES_DEFINITIONS.put(name, new Definition(name, baseTemplate, attributes));
    }
}
