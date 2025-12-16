package com.biblioteca.musical.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.biblioteca.musical.model.Song;
import com.biblioteca.musical.service.BibliotecaMusicalService;

public class BibliotecaMusicalClient extends JFrame {
    
    private static final String WSDL_URL = "http://localhost:8080/BibliotecaMusical?wsdl";
    private BibliotecaMusicalService service;
    
    private JTextField campoNombre;
    private JTextField campoGenero;
    private JTextField campoAutor;
    private JTextArea areaResultados;
    private JButton botonBuscar;
    private JButton botonLimpiar;
    private JButton botonTodas;
    
    public BibliotecaMusicalClient() {
        super("Cliente Biblioteca Musical SOAP");
        inicializarServicio();
        crearInterfaz();
        configurarEventos();
    }
    
    private void inicializarServicio() {
        try {
            URL url = new URL(WSDL_URL);
            QName qname = new QName("http://service.musical.biblioteca.com/", "BibliotecaMusicalServiceImplService");
            Service ws = Service.create(url, qname);
            service = ws.getPort(BibliotecaMusicalService.class);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al conectar con el servidor SOAP: " + e.getMessage(), 
                "Error de Conexión", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void crearInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel panelBusqueda = crearPanelBusqueda();
        JPanel panelBotones = crearPanelBotones();
        JPanel panelResultados = crearPanelResultados();
        
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(panelResultados, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Criterios de Búsqueda"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        campoNombre = new JTextField(20);
        panel.add(campoNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1;
        campoGenero = new JTextField(20);
        panel.add(campoGenero, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1;
        campoAutor = new JTextField(20);
        panel.add(campoAutor, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout());
        
        botonBuscar = new JButton("Buscar");
        botonLimpiar = new JButton("Limpiar");
        botonTodas = new JButton("Mostrar Todas");
        
        panel.add(botonBuscar);
        panel.add(botonLimpiar);
        panel.add(botonTodas);
        
        return panel;
    }
    
    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Resultados"));
        
        areaResultados = new JTextArea(15, 50);
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void configurarEventos() {
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarBusqueda();
            }
        });
        
        botonLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
        
        botonTodas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTodasLasCanciones();
            }
        });
    }
    
    private void realizarBusqueda() {
        try {
            String nombre = campoNombre.getText().trim();
            String genero = campoGenero.getText().trim();
            String autor = campoAutor.getText().trim();
            
            List<Song> resultados;
            
            if (!nombre.isEmpty() || !genero.isEmpty() || !autor.isEmpty()) {
                resultados = service.buscarPorCriterios(
                    nombre.isEmpty() ? null : nombre,
                    genero.isEmpty() ? null : genero,
                    autor.isEmpty() ? null : autor
                );
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, ingrese al menos un criterio de búsqueda.", 
                    "Información", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            mostrarResultados(resultados);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al realizar la búsqueda: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarTodasLasCanciones() {
        try {
            List<Song> todas = service.obtenerTodasLasCanciones();
            mostrarResultados(todas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al obtener todas las canciones: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarResultados(List<Song> canciones) {
        StringBuilder sb = new StringBuilder();
        
        if (canciones == null || canciones.isEmpty()) {
            sb.append("No se encontraron resultados.\n");
        } else {
            sb.append(String.format("Se encontraron %d canción(es):\n\n", canciones.size()));
            sb.append(String.format("%-30s %-20s %-15s %-10s %s\n", 
                "TÍTULO", "AUTOR", "GÉNERO", "IDIOMA", "AÑO"));
            sb.append("=".repeat(90)).append("\n");
            
            for (Song cancion : canciones) {
                sb.append(String.format("%-30s %-20s %-15s %-10s %d\n",
                    truncar(cancion.getTitulo(), 30),
                    truncar(cancion.getAutor(), 20),
                    truncar(cancion.getGenero(), 15),
                    cancion.getIdioma(),
                    cancion.getAnoLanzamiento()));
            }
        }
        
        areaResultados.setText(sb.toString());
        areaResultados.setCaretPosition(0);
    }
    
    private String truncar(String texto, int maxLength) {
        if (texto == null) return "";
        return texto.length() > maxLength ? texto.substring(0, maxLength - 3) + "..." : texto;
    }
    
    private void limpiarCampos() {
        campoNombre.setText("");
        campoGenero.setText("");
        campoAutor.setText("");
        areaResultados.setText("");
        campoNombre.requestFocus();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BibliotecaMusicalClient().setVisible(true);
            }
        });
    }
}