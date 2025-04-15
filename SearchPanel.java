package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import models.Book;

/**
 * Search panel for searching books
 */
public class SearchPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField searchField;
    private JComboBox<String> searchTypeComboBox;
    private JTable resultsTable;
    private DefaultTableModel resultsModel;
    
    private MainFrame parentFrame;
    
    /**
     * Create search panel
     * @param parent Parent frame
     */
    public SearchPanel(MainFrame parent) {
        this.parentFrame = parent;
        initComponents();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        searchPanel.add(new JLabel("Търсене по:"));
        
        String[] searchTypes = {"title", "author", "genre"};
        String[] searchTypesDisplay = {"Заглавие", "Автор", "Жанр"};
        searchTypeComboBox = new JComboBox<>(searchTypesDisplay);
        searchPanel.add(searchTypeComboBox);
        
        searchField = new JTextField(30);
        searchPanel.add(searchField);
        
        JButton searchButton = new JButton("Търси");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });
        searchPanel.add(searchButton);
        
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showDashboard();
            }
        });
        searchPanel.add(backButton);
        
        add(searchPanel, BorderLayout.NORTH);
        
        // Results table
        String[] columns = {"ID", "Заглавие", "Автор", "Жанр", "Статус"};
        resultsModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultsTable = new JTable(resultsModel);
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultsTable.getColumnModel().getColumn(0).setMaxWidth(50);
        
        // Double-click to view book details
        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = resultsTable.getSelectedRow();
                    if (row >= 0) {
                        int bookId = (int) resultsTable.getValueAt(row, 0);
                        parentFrame.showBookPanel(bookId);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Action panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton viewButton = new JButton("Преглед");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = resultsTable.getSelectedRow();
                if (row >= 0) {
                    int bookId = (int) resultsTable.getValueAt(row, 0);
                    parentFrame.showBookPanel(bookId);
                } else {
                    JOptionPane.showMessageDialog(SearchPanel.this, 
                            "Моля, изберете книга от резултатите", 
                            "Информация", 
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        actionPanel.add(viewButton);
        
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Search books
     */
    private void searchBooks() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Моля, въведете текст за търсене", 
                    "Информация", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] searchTypes = {"title", "author", "genre"};
        String searchType = searchTypes[searchTypeComboBox.getSelectedIndex()];
        
        BookDAO bookDAO = new BookDAO();
        List<Book> results = bookDAO.searchBooks(searchType, searchTerm);
        
        // Clear results table
        resultsModel.setRowCount(0);
        
        // Add results to table
        for (Book book : results) {
            resultsModel.addRow(new Object[] {
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getAvailability()
            });
        }
        
        // Show message if no results
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Няма намерени резултати", 
                    "Информация", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Refresh panel data
     */
    public void refreshData() {
        // Clear search field and results
        searchField.setText("");
        resultsModel.setRowCount(0);
    }
}