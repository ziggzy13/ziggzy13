package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import utils.SessionManager;

/**
 * Main application frame
 */
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private DashboardPanel dashboardPanel;
    private SearchPanel searchPanel;
    private BookPanel bookPanel;
    private LoanPanel loanPanel;
    private AdminPanel adminPanel;
    
    private JMenuBar menuBar;
    
    /**
     * Create main frame
     */
    public MainFrame() {
        setTitle("Библиотечна система");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        
        // Try to set Nimbus look and feel
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fallback to default look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        }
        
        initComponents();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Cleanup code here if needed
                System.exit(0);
            }
        });
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Create menu bar
        createMenuBar();
        
        // Create content panel with card layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Create panels
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        dashboardPanel = new DashboardPanel(this);
        searchPanel = new SearchPanel(this);
        bookPanel = new BookPanel(this);
        loanPanel = new LoanPanel(this);
        adminPanel = new AdminPanel(this);
        
        // Add panels to content panel
        contentPanel.add(loginPanel, "login");
        contentPanel.add(registerPanel, "register");
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(searchPanel, "search");
        contentPanel.add(bookPanel, "book");
        contentPanel.add(loanPanel, "loan");
        contentPanel.add(adminPanel, "admin");
        
        // Show login panel by default
        cardLayout.show(contentPanel, "login");
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create menu bar
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("Файл");
        
        JMenuItem exitMenuItem = new JMenuItem("Изход");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, 
                        "Библиотечна система\nВерсия 1.0\nРазработена за упражнение по софтуерно инженерство\n",
                        "За програмата", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpMenu.add(aboutMenuItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(navigationMenu);
        menuBar.add(adminMenu);
        menuBar.add(helpMenu);
        
        // Set menu bar
        setJMenuBar(menuBar);
        
        // Update menu visibility based on login status
        updateMenuVisibility();
    }
    
    /**
     * Update menu visibility based on login status
     */
    public void updateMenuVisibility() {
        boolean loggedIn = SessionManager.isLoggedIn();
        boolean isAdmin = SessionManager.isAdmin();
        
        // Get navigation menu
        JMenu navigationMenu = menuBar.getMenu(1);
        navigationMenu.setVisible(loggedIn);
        
        // Get admin menu
        JMenu adminMenu = menuBar.getMenu(2);
        adminMenu.setVisible(isAdmin);
    }
    
    /**
     * Show login panel
     */
    public void showLoginPanel() {
        cardLayout.show(contentPanel, "login");
        updateMenuVisibility();
    }
    
    /**
     * Show register panel
     */
    public void showRegisterPanel() {
        cardLayout.show(contentPanel, "register");
    }
    
    /**
     * Show dashboard panel
     */
    public void showDashboard() {
        dashboardPanel.refreshData();
        cardLayout.show(contentPanel, "dashboard");
        updateMenuVisibility();
    }
    
    /**
     * Show search panel
     */
    public void showSearchPanel() {
        searchPanel.refreshData();
        cardLayout.show(contentPanel, "search");
    }
    
    /**
     * Show book details panel
     * @param bookId Book ID to display
     */
    public void showBookPanel(int bookId) {
        bookPanel.loadBook(bookId);
        cardLayout.show(contentPanel, "book");
    }
    
    /**
     * Show loan panel
     */
    public void showLoanPanel() {
        loanPanel.refreshData();
        cardLayout.show(contentPanel, "loan");
    }
    
    /**
     * Show admin panel
     */
    public void showAdminPanel() {
        adminPanel.refreshData();
        cardLayout.show(contentPanel, "admin");
    }
}Event e) {
                int result = JOptionPane.showConfirmDialog(MainFrame.this, 
                        "Наистина ли искате да излезете?", 
                        "Потвърждение", 
                        JOptionPane.YES_NO_OPTION);
                
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        fileMenu.add(exitMenuItem);
        
        // Navigation menu
        JMenu navigationMenu = new JMenu("Навигация");
        
        JMenuItem dashboardMenuItem = new JMenuItem("Начало");
        dashboardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SessionManager.isLoggedIn()) {
                    showDashboard();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, 
                            "Трябва да влезете в системата първо", 
                            "Информация", 
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        navigationMenu.add(dashboardMenuItem);
        
        JMenuItem searchMenuItem = new JMenuItem("Търсене на книги");
        searchMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SessionManager.isLoggedIn()) {
                    showSearchPanel();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, 
                            "Трябва да влезете в системата първо", 
                            "Информация", 
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        navigationMenu.add(searchMenuItem);
        
        JMenuItem loansMenuItem = new JMenuItem("Моите заемания");
        loansMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SessionManager.isLoggedIn()) {
                    showLoanPanel();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, 
                            "Трябва да влезете в системата първо", 
                            "Информация", 
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        navigationMenu.add(loansMenuItem);
        
        // Admin menu
        JMenu adminMenu = new JMenu("Администрация");
        
        JMenuItem adminPanelMenuItem = new JMenuItem("Административен панел");
        adminPanelMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SessionManager.isAdmin()) {
                    showAdminPanel();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, 
                            "Нямате достъп до тази функционалност", 
                            "Грешка", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        adminMenu.add(adminPanelMenuItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Помощ");
        
        JMenuItem aboutMenuItem = new JMenuItem("За програмата");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(Action