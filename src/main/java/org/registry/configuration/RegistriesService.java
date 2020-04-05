package org.registry.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Access registries from official docker plugin
 */
@com.intellij.openapi.components.State(
        name = "DockerRegistry",
        storages = {@Storage(value = "docker-registry.xml")}
)
public class RegistriesService implements PersistentStateComponent<State> {

    private State registries;

    @Nullable
    @Override
    public State getState() {
        return registries;
    }

    @Override
    public void loadState(@NotNull State state) {
        registries = state;
    }
}
