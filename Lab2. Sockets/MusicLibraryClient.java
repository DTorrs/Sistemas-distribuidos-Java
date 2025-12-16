package main;

import classes.Client;
import classes.Song;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Clase principal para iniciar el cliente de la biblioteca de música con GUI
 */
public class MusicLibraryClient extends JFrame {
    
    private Client client;
    
    // Componentes de la interfaz gráfica
    private JTextField txtTitle, txtGenre, txtAuthor;
    private JButton btnSearch, btnShowAll, btnExit;
    private JTextArea txtResults;
    private JPanel pnlSearch, pnlButtons, pnlResults;
    
    /**
     * Constructor que inicializa el cliente y la interfaz gráfica
     */
    public MusicLibraryClient() {
        super("Biblioteca de Música - Cliente");
        
        // Inicializar el cliente
        client = new Client("127.0.0.1", 1802);
        
        // Inicializar la interfaz gráfica
        initializeGUI();
    }
    
    /**
     * Inicializa la interfaz gráfica
     */
    private void initializeGUI() {
        // Configuración básica de la ventana
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Panel de búsqueda
        pnlSearch = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlSearch.setBorder(BorderFactory.createTitledBorder("Criterios de Búsqueda"));
        
        pnlSearch.add(new JLabel("Título:"));
        txtTitle = new JTextField(20);
        pnlSearch.add(txtTitle);
        
        pnlSearch.add(new JLabel("Género:"));
        txtGenre = new JTextField(20);
        pnlSearch.add(txtGenre);
        
        pnlSearch.add(new JLabel("Autor:"));
        txtAuthor = new JTextField(20);
        pnlSearch.add(txtAuthor);
        
        // Panel de botones
        pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSearch = new JButton("Buscar");
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchSongs();
            }
        });
        pnlButtons.add(btnSearch);
        
        btnShowAll = new JButton("Mostrar Todas");
        btnShowAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllSongs();
            }
        });
        pnlButtons.add(btnShowAll);
        
        btnExit = new JButton("Salir");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        });
        pnlButtons.add(btnExit);
        
        // Panel de resultados
        pnlResults = new JPanel(new BorderLayout());
        pnlResults.setBorder(BorderFactory.createTitledBorder("Resultados"));
        
        txtResults = new JTextArea(15, 50);
        txtResults.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResults);
        pnlResults.add(scrollPane, BorderLayout.CENTER);
        
        // Añadir paneles a la ventana
        add(pnlSearch, BorderLayout.NORTH);
        add(pnlButtons, BorderLayout.CENTER);
        add(pnlResults, BorderLayout.SOUTH);
        
        // Agregar un WindowListener para cerrar la conexión al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.closeConnection();
            }
        });
    }
    
    /**
     * Busca canciones según los criterios ingresados
     */
    private void searchSongs() {
        try {
            String title = txtTitle.getText().trim();
            String genre = txtGenre.getText().trim();
            String author = txtAuthor.getText().trim();
            
            // Si todos los campos están vacíos, mostrar un mensaje
            if (title.isEmpty() && genre.isEmpty() && author.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, ingrese al menos un criterio de búsqueda.", 
                    "Campos Vacíos", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Realizar la búsqueda
            List<Song> results = client.searchSongs(title, genre, author);
            displaySongs(results);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al buscar canciones: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra todas las canciones disponibles
     */
    private void showAllSongs() {
        try {
            List<Song> allSongs = client.getAllSongs();
            displaySongs(allSongs);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al obtener todas las canciones: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra la lista de canciones en el área de resultados
     * @param songs Lista de canciones a mostrar
     */
    private void displaySongs(List<Song> songs) {
        txtResults.setText(""); // Limpiar resultados anteriores
        
        if (songs == null || songs.isEmpty()) {
            txtResults.append("No se encontraron canciones con los criterios especificados.\n");
        } else {
            txtResults.append("Se encontraron " + songs.size() + " canciones:\n\n");
            
            for (Song song : songs) {
                txtResults.append(song.toString() + "\n");
            }
        }
    }
    
    /**
     * Cierra la conexión y la aplicación
     */
    private void exitApplication() {
        client.closeConnection();
        dispose();
        System.exit(0);
    }
    
    /**
     * Método principal para iniciar el cliente
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MusicLibraryClient client = new MusicLibraryClient();
                client.setVisible(true);
            }
        });
    }
}