package com.asciugano.engine.entities;

import com.asciugano.engine.components.Component;
import com.asciugano.engine.components.ComponentType;
import com.asciugano.engine.components.EntityTransform;
import com.asciugano.game.scene.Scene;

import java.util.LinkedHashMap;
import java.util.Map;

public class Blueprint {

    public final String id;
    public final boolean savingEnabled;

    private final Map<ComponentType, ComponentBlueprint> components;

    public Blueprint(String id, boolean savingEnabled, Map<ComponentType, ComponentBlueprint> components) {
        this.id = id;
        this.savingEnabled = savingEnabled;
        this.components = components;
    }

    public Blueprint(String id, boolean savingEnabled, ComponentBlueprint... compBlueprints) {
        this.id = id;
        this.savingEnabled = savingEnabled;
        this.componentTypes = initComponentMap(compBlueprints);
    }

    public ComponentBlueprint getComponent(ComponentType type) { return components.get(type); }

    public Entity createInstance(Scene scene, ComponentParams... params) {
        return createInstance(scene, new EntityTransform(), params);
    }

    public Entity createInstance(Scene scene, EntityTransform transform, ComponentParams... params) {
        Entity entity = new Entity(this, scene, transform);
        ComponentBundle bundle = createEntityComponents(entity, params);
        entity.setComponents(bundle.getComponents());
        return entity;
    }

    private ComponentBundle createEntityComponents(Entity entity, ComponentParams... params) {
        ComponentBundle bundle = new ComponentBundle(params);
        for(ComponentBlueprint component : components.values()) {
            bundle.addComponent(bundle.getComponents());
        }
        return bundle;
    }

    private static Map<ComponentType, ComponentBlueprint> initComponentMap(ComponentBlueprint[] components) {
        Map<ComponentType, ComponentBlueprint> componentMap = new LinkedHashMap<>();
        for(ComponentBlueprint component : components) {

        }
    }
}