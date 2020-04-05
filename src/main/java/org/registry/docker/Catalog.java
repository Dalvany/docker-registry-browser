package org.registry.docker;

import org.registry.ui.FilterableTreeNode;
import org.registry.ui.TreeData;

import javax.swing.tree.TreeNode;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

public class Catalog {
    private Collection<String> repositories = Collections.emptyList();

    public Collection<String> getRepositories() {
        return repositories;
    }

    public FilterableTreeNode asTreeNode() {
        FilterableTreeNode root = new FilterableTreeNode(new TreeData("", ""));
        for (String s : repositories) {
            StringBuilder path = new StringBuilder();
            String[] parts = s.split("/");
            FilterableTreeNode current = root;
            for (String s2 : parts) {
                path.append(s2);
                TreeData td = new TreeData(s2, path.toString());
                Enumeration<TreeNode> nodes = current.children();
                boolean found = false;
                while (nodes.hasMoreElements() && !found) {
                    FilterableTreeNode tn = (FilterableTreeNode) nodes.nextElement();
                    if (tn.getUserObject() != null && tn.getUserObject().equals(td)) {
                        current = tn;
                        found = true;
                    }
                }
                if (!found) {
                    FilterableTreeNode n = new FilterableTreeNode(td);
                    current.add(n);
                    current = n;
                }
                path.append('/');
            }
        }

        return root;
    }
}
