/**
 * 
 */
/**
 * 
 */
module LucasLibrary {
	requires transitive java.desktop;
	requires transitive java.json;
    exports com.lucasj.lucaslibrary.game;
    exports com.lucasj.lucaslibrary.game.interfaces;
    exports com.lucasj.lucaslibrary.game.world;
    exports com.lucasj.lucaslibrary.game.objects;
    exports com.lucasj.lucaslibrary.game.objects.components;
    exports com.lucasj.lucaslibrary.game.objects.components.player_controller;
    exports com.lucasj.lucaslibrary.game.objects.components.physics;
    exports com.lucasj.lucaslibrary.game.objects.components.rendering;
    exports com.lucasj.lucaslibrary.game.objects.components.utility;
    exports com.lucasj.lucaslibrary.math;
    exports com.lucasj.lucaslibrary.log;
    exports com.lucasj.lucaslibrary.events;
    exports com.lucasj.lucaslibrary.events.component_events;
    exports com.lucasj.lucaslibrary.events.game;
    exports com.lucasj.lucaslibrary.events.gameobjects;
    exports com.lucasj.lucaslibrary.events.input;
    exports com.lucasj.lucaslibrary.events.input.handler;
    exports com.lucasj.lucaslibrary.events.physics;
    exports com.lucasj.lucaslibrary.utils;
    exports com.lucasj.lucaslibrary.UI;
    exports com.lucasj.lucaslibrary.UI.interfaces;
    exports com.lucasj.lucaslibrary.UI.utils;
}
