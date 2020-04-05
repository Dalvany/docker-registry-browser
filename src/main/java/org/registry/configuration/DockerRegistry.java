package org.registry.configuration;

import com.intellij.util.xmlb.annotations.Attribute;

import java.util.StringJoiner;

public class DockerRegistry {
    @Attribute
    public String email;
    public String address;
    public String name;
    public String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DockerRegistry)) return false;

        DockerRegistry registry = (DockerRegistry) o;

        if (!email.equals(registry.email)) return false;
        if (!address.equals(registry.address)) return false;
        if (!name.equals(registry.name)) return false;
        return username.equals(registry.username);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DockerRegistry.class.getSimpleName() + "[", "]")
                .add("email='" + email + "'")
                .add("address='" + address + "'")
                .add("name='" + name + "'")
                .add("username='" + username + "'")
                .toString();
    }
}
