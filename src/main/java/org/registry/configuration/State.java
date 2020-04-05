package org.registry.configuration;

import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.XCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class State {
    @Property(
            surroundWithTag = false
    )
    @XCollection
    public List<DockerRegistry> registries = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;

        State state = (State) o;

        return Objects.equals(registries, state.registries);
    }

    @Override
    public int hashCode() {
        return registries != null ? registries.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", State.class.getSimpleName() + "[", "]")
                .add("registries=" + registries)
                .toString();
    }
}
