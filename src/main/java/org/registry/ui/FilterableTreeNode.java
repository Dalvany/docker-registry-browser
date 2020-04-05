package org.registry.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;

public class FilterableTreeNode extends DefaultMutableTreeNode implements Comparable<TreeNode> {
    private Vector<TreeNode> filtered;

    public FilterableTreeNode(Object userObject) {
        super(userObject);
        filtered = new Vector<>();
        children = new Vector<>();
    }

    public boolean filter(Collection<String> tokens) {
        TreeSet<TreeNode> all = new TreeSet<>(filtered);
        all.addAll(children);
        removeAllChildren();
        filtered.removeAllElements();
        boolean result = false;
        Collection<String> tmp = tokens;
        if (((TreeData) userObject).pathContainsAll(tokens)) {
            tmp = null;
            result = true;
        }

        for (TreeNode tn : all) {
            if (!tn.getClass().equals(FilterableTreeNode.class) || !((FilterableTreeNode) tn).filter(tmp)) {
                filtered.add(tn);
            } else {
                add((FilterableTreeNode) tn);
                result = true;
            }
        }

        return result;
    }

    @Override
    public int compareTo(@NotNull TreeNode o) {
        boolean hasNoUserObject = (!(o instanceof DefaultMutableTreeNode) || ((DefaultMutableTreeNode) o).getUserObject() == null);
        if (userObject == null && hasNoUserObject) {
            return 0;
        }
        if (userObject == null) {
            return -1;
        }
        if (hasNoUserObject) {
            return 1;
        }
        return userObject.toString().compareTo(((DefaultMutableTreeNode) o).getUserObject().toString());
    }
}
