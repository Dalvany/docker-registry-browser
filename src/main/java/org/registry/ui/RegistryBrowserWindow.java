package org.registry.ui;


import com.google.common.base.Joiner;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.SearchTextField;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.treeStructure.Tree;
import org.registry.configuration.BrowsableRegistry;
import org.registry.configuration.DockerRegistry;
import org.registry.configuration.RegistriesService;
import org.registry.configuration.State;
import org.registry.docker.Catalog;
import org.registry.docker.Image;
import org.registry.docker.Tag;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;

public class RegistryBrowserWindow extends JComponent {

    private static final Logger LOGGER = Logger.getInstance(RegistryBrowserWindow.class);
    /**
     * Root panel
     */
    private JPanel panel1;
    /**
     * Registry selection
     */
    private ComboBox<BrowsableRegistry> comboBox1;
    /**
     * Filter image tree
     */
    private SearchTextField searchTextField1;
    /**
     * Docker image tree
     */
    private Tree tree1;
    /**
     * Selected image tag list
     */
    private JBList jbList1;
    /**
     * Select image tag info
     */
    private JBTextArea jbTextArea1;
    private DefaultListModel<String> listTags;

    public RegistryBrowserWindow() {
        RegistriesService service = ServiceManager.getService(RegistriesService.class);
        State s = service.getState();
        if (s != null) {
            Set<BrowsableRegistry> registries = new TreeSet<>();
            for (DockerRegistry dr : s.registries) {
                registries.add(new BrowsableRegistry(dr));
            }
            DefaultComboBoxModel<BrowsableRegistry> model = new DefaultComboBoxModel<>();
            model.addAll(registries);
            comboBox1.setModel(model);
            comboBox1.addActionListener(this::onSelectRegistryListener);
            comboBox1.setSelectedIndex(0);
        }

        tree1.addTreeSelectionListener(this::onSelectImageListener);
        TreeSelectionModel tsm = new DefaultTreeSelectionModel();
        tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree1.setSelectionModel(tsm);

        listTags = new DefaultListModel<>();
        jbList1.setModel(listTags);
        jbList1.addListSelectionListener(this::onSelectTagListener);

        // Filter tree if text change (allow filter as typing)
        searchTextField1.addDocumentListener(new DocumentListener() {
            private void search() {
                Collection<String> tokens = new ArrayList<>();
                if (searchTextField1.getText() != null) {
                    String[] tmp = searchTextField1.getText().split("\\s");
                    tokens = Arrays.asList(tmp);
                }
                tree1.clearSelection();
                ((FilterableTreeNode) tree1.getModel().getRoot()).filter(tokens);
                ((DefaultTreeModel) tree1.getModel()).reload();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }
        });
    }

    private <E> Optional<E> handle(Call<E> call) {
        try {
            Response<E> response = call.execute();
            if (response.isSuccessful()) {
                return Optional.ofNullable(response.body());
            }
            LOGGER.error("Unable to call api", call.toString(), Integer.toString(response.code()), response.message());
        } catch (IOException e) {
            LOGGER.error("Unable to call api", e);
        }
        return Optional.empty();
    }

    private void onSelectRegistryListener(ActionEvent actionEvent) {
        BrowsableRegistry s = (BrowsableRegistry) comboBox1.getSelectedItem();
        if (s != null) {
            Call<Catalog> call = s.getService().getCatalog();
            Optional<Catalog> catalog = handle(call);
            catalog.ifPresent(value -> tree1.setModel(new DefaultTreeModel(value.asTreeNode())));
        }
    }

    private void onSelectImageListener(TreeSelectionEvent actionEvent) {
        BrowsableRegistry s = (BrowsableRegistry) comboBox1.getSelectedItem();
        if (s != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
            if (node == null || !node.isLeaf()) {
                jbList1.setSelectedIndex(-1);
                listTags.removeAllElements();
                jbTextArea1.setText("");
                return;
            }

            String imageName = ((TreeData) node.getUserObject()).getPath();

            Call<Image> call = s.getService().getImage(imageName);
            Optional<Image> image = handle(call);
            if (image.isPresent()) {
                listTags.removeAllElements();
                listTags.addAll(image.get().getTags());
            }
        }
    }

    private void onSelectTagListener(ListSelectionEvent listSelectionEvent) {
        BrowsableRegistry s = (BrowsableRegistry) comboBox1.getSelectedItem();
        if (s != null && jbList1.getSelectedValue() != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
            TreeNode[] nodes = node.getPath();
            String imageName = Joiner.on('/').join(Arrays.copyOfRange(nodes, 1, nodes.length));
            String tagName = (String) jbList1.getSelectedValue();

            Call<Tag> call = s.getService().getTag(imageName, tagName);
            Optional<Tag> tag = handle(call);
            tag.ifPresent(value -> jbTextArea1.setText(value.getText(s.getHost())));
        }
    }

    public JPanel getContent() {
        return panel1;
    }
}
