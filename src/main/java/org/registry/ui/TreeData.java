package org.registry.ui;

import java.util.Collection;

public class TreeData {
    private String label;
    private String path;

    public TreeData(String label, String path) {
        this.label = label;
        this.path = path;
    }

    public String getLabel() {
        return label;
    }

    public String getPath() {
        return path;
    }

    public boolean pathContainsAll(Collection<String> tokens) {
        return tokens == null || tokens.isEmpty() || tokens.stream().filter(s -> path.toLowerCase().contains(s.toLowerCase())).count() == tokens.size();
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeData)) return false;

        TreeData treeData = (TreeData) o;

        return path.equals(treeData.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
