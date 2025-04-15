package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.LoanDAO;
import models.Loan;
import utils.SessionManager;

/**
 * Loan panel for managing user loans
 */
public class LoanPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTable activeLoansTable;
    private JTable pastLoansTable;
    private DefaultTableModel activeLoansModel;
    private DefaultTableModel pastLoansModel;
    
    private MainFrame parentFrame;
    
    /**
     * Create loan panel
     * @param parent Parent frame
     */
    public LoanPanel(MainFrame parent) {
        this.parentFrame = parent;
        initComponents();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Моите заемания", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(24.0f));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showDashboard();
            }
        });
        titlePanel.add(backButton, BorderLayout.EAST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Active loans panel
        JPanel activeLoansPanel = new JPanel(new BorderLayout());
        
        String[] activeColumns = {"ID", "Книга", "Автор", "Дата на заемане", "Дата на връщане", "Статус"};
        activeLoansModel = new DefaultTableModel(activeColumns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        activeLoansTable = new JTable(activeLoansModel);
        activeLoansTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane activeScrollPane = new JScrollPane(activeLoansTable);
        activeLoansPanel.add(activeScrollPane, BorderLayout.CENTER);
        
        JPanel activeButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton returnButton = new JButton("Върни избраната книга");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });
        activeButtonsPanel.add(returnButton);
        
        activeLoansPanel.add(activeButtonsPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Активни заемания", activeLoansPanel);
        
        // Past loans panel
        JPanel pastLoansPanel = new JPanel(new BorderLayout());
        
        String[] pastColumns = {"ID", "Книга", "Автор", "Дата на заемане", "Дата на връщане", "Действителна дата на връщане"};
        pastLoansModel = new DefaultTableModel(pastColumns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        pastLoansTable = new JTable(pastLoansModel);
        pastLoansTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane pastScrollPane = new JScrollPane(pastLoansTable);
        pastLoansPanel.add(pastScrollPane, BorderLayout.CENTER);
        
        tabbedPane.addTab("История на заеманията", pastLoansPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Return selected book
     */
    private void returnBook() {
        int row = activeLoansTable.getSelectedRow();
        
        if (row < 0) {
            JOptionPane.showMessageDialog(this, 
                    "Моля, изберете заемане за връщане", 
                    "Информация", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int loanId = (int) activeLoansTable.getValueAt(row, 0);
        String bookTitle = (String) activeLoansTable.getValueAt(row, 1);
        
        int result = JOptionPane.showConfirmDialog(this, 
                "Искате ли да върнете книгата \"" + bookTitle + "\"?", 
                "Потвърждение", 
                JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            LoanDAO loanDAO = new LoanDAO();
            boolean success = loanDAO.returnBook(loanId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                        "Книгата е успешно върната", 
                        "Успех", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh loan data
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Възникна грешка при връщането на книгата", 
                        "Грешка", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Check if loan is overdue
     * @param returnDateStr Return date string
     * @return true if loan is overdue, false otherwise
     */
    private boolean isOverdue(String returnDateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date returnDate = sdf.parse(returnDateStr);
            Date currentDate = new Date();
            
            return currentDate.after(returnDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Refresh panel data
     */
    public void refreshData() {
        if (!SessionManager.isLoggedIn()) {
            return;
        }
        
        // Clear tables
        activeLoansModel.setRowCount(0);
        pastLoansModel.setRowCount(0);
        
        // Load loans for current user
        LoanDAO loanDAO = new LoanDAO();
        List<Loan> activeLoans = loanDAO.getLoansByUserId(SessionManager.getCurrentUser().getUserId(), true);
        List<Loan> pastLoans = loanDAO.getLoansByUserId(SessionManager.getCurrentUser().getUserId(), false);
        
        // Add active loans to table
        for (Loan loan : activeLoans) {
            boolean overdue = isOverdue(loan.getReturnDate());
            String status = overdue ? "Просрочена" : "Активна";
            
            activeLoansModel.addRow(new Object[] {
                loan.getLoanId(),
                loan.getBookTitle(),
                loan.getBookAuthor(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                status
            });
        }
        
        // Add past loans to table
        for (Loan loan : pastLoans) {
            pastLoansModel.addRow(new Object[] {
                loan.getLoanId(),
                loan.getBookTitle(),
                loan.getBookAuthor(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.getActualReturnDate()
            });
        }
    }
}