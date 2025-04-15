package ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.UserDAO;
import models.User;
import utils.SessionManager;

/**
 * Login panel for user authentication
 */
public class LoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    private MainFrame parentFrame;
    
    /**
     * Create login panel
     * @param parent Parent frame
     */
    public LoginPanel(MainFrame parent) {
        this.parentFrame = parent;
        initComponents();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Вход в системата", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(24.0f));
        add(titleLabel, BorderLayout.NORTH);
        
        // Login form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Email field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Имейл:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Парола:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Вход");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        formPanel.add(loginButton, gbc);
        
        // Register button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        registerButton = new JButton("Регистрация");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showRegisterPanel();
            }
        });
        formPanel.add(registerButton, gbc);
        
        add(formPanel, BorderLayout.CENTER);
    }
    
    /**
     * Attempt to login with email and password
     */
    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Моля, попълнете имейл и парола", "Грешка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(email, password);
        
        if (user != null) {
            SessionManager.setCurrentUser(user);
            parentFrame.showDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Невалиден имейл или парола", "Грешка", JOptionPane.ERROR_MESSAGE);
        }
    }
}