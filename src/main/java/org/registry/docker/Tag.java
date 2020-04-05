package org.registry.docker;

import java.util.Collection;
import java.util.Collections;

public class Tag {
    private int schemaVersion;
    private String name;
    private String tag;
    private String architecture;
    private Collection<History> history = Collections.emptyList();

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getArchitecture() {
        return architecture;
    }

    public Collection<History> getHistory() {
        return history;
    }

    public String getText(String host) {
        return "Architecture : " + architecture + '\n' +
                "Image : " + name + '\n' +
                "Tag : " + tag + '\n' +
                "Pull : " + host + '/' + name + ':' + tag;
    }
}
