package com.rescue.controller;

import com.rescue.model.User;
import com.rescue.repo.UserRepository;
import com.rescue.view.UserManagementView;
import javax.swing.*;
import java.util.List;

public class UserManagementController {
    private final UserManagementView view;
    private final UserRepository repo;

    public UserManagementController(UserManagementView view, UserRepository repo) {
        this.view = view;
        this.repo = repo;
        loadUsers();
        initListeners();
    }

    private void loadUsers() {
        view.getTableModel().setRowCount(0);
        List<User> users = repo.findAll();
        for (User u : users) {
            view.getTableModel().addRow(new Object[]{u.getId(), u.getUsername(), u.getRole()});
        }
    }

    private void initListeners() {
        view.getAddBtn().addActionListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();
            String role = view.getRole();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Username and password cannot be empty.");
                return;
            }
            int id = repo.nextId();
            repo.save(new User(id, username, password, role));
            loadUsers();
            view.clearFields();
        });

        view.getUpdateBtn().addActionListener(e -> {
            int selectedRow = view.getUserTable().getSelectedRow();
            if (selectedRow == -1) return;

            int id = (int) view.getTableModel().getValueAt(selectedRow, 0);
            String username = view.getUsername();
            String password = view.getPassword();
            String role = view.getRole();

            repo.findById(id).ifPresent(u -> {
                u.setUsername(username);
                if (!password.isEmpty()) u.setPassword(password);
                u.setRole(role);
                repo.save(u);
            });
            loadUsers();
            view.clearFields();
        });

        view.getDeleteBtn().addActionListener(e -> {
            int selectedRow = view.getUserTable().getSelectedRow();
            if (selectedRow == -1) return;

            int id = (int) view.getTableModel().getValueAt(selectedRow, 0);
            repo.delete(id);
            loadUsers();
            view.clearFields();
        });

        view.getUserTable().getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = view.getUserTable().getSelectedRow();
            if (selectedRow != -1) {
                String username = (String) view.getTableModel().getValueAt(selectedRow, 1);
                String role = (String) view.getTableModel().getValueAt(selectedRow, 2);
                view.setFields(username, role);
            }
        });

        view.getBackBtn().addActionListener(e -> view.dispose());
    }
}