package org.registry.docker;

import java.util.Collection;
import java.util.Collections;

public class Image {
    private Collection<String> tags = Collections.emptyList();

    public Collection<String> getTags() {
        return tags;
    }
}
