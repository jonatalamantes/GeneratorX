import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class VentanaPrincipal 
{
	private JFrame frame;
	private JTextField txtAgregarClase;
	private JTextField txtAgregarAtributo;
	private JList<String> listaClases;
	private JList<String> listaAtributos; 
	private JFileChooser chooser = new JFileChooser();

	private LinkedList<Clase> clases;
	private JPanel panelClase;
	private JPanel panelAtributo;
	private JCheckBox checkRelacion;
	private JLabel lblNombreClase;
	private int claseActual = -1;
	private int atributoActual = -1;
	private JTextField txtNombreAtributo;
	private JTextField txtDescripcion;
	private JTextField txtDefecto;
	private JComboBox<String> comboTipo;
	private JCheckBox checkForanea;
	private JCheckBox checkPrimaria;
	private JCheckBox checkDisimil;
	private JCheckBox checkNula;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					VentanaPrincipal window = new VentanaPrincipal();
					window.frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	void actualizar()
	{
		 DefaultListModel<String> lModel = new DefaultListModel<String>();
		 
		 for (int i = 0; i < clases.size(); i++)
		 {
			lModel.addElement(clases.get(i).getNombre());
		 }
		 
		 listaClases.setModel(lModel);
		 listaClases.setSelectedIndex(-1);

		 if (claseActual != -1)
		 {
			 Clase miClase = clases.get(claseActual);
			 
			 DefaultListModel<String> aModel = new DefaultListModel<String>();

			 for (int i = 0; i < miClase.getAtributos().size(); i++)
			 {
				 aModel.addElement(miClase.getAtributos().get(i).getNombre());
			 }
			 
			 listaAtributos.setModel(aModel);
		 }
		 else
		 {
			 DefaultListModel<String> aModel = new DefaultListModel<String>();
			 listaAtributos.setModel(aModel);
			 listaAtributos.setSelectedIndex(-1);
		 }
	}

	/**
	 * Create the application.
	 */
	public VentanaPrincipal() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		clases = new LinkedList<Clase>();
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblClase = new JLabel("Clases");
		lblClase.setHorizontalAlignment(SwingConstants.CENTER);
		lblClase.setBounds(40, 20, 140, 20);
		panel.add(lblClase);
		
		listaClases = new JList<String>();
		listaClases.setBackground(SystemColor.controlHighlight);
		listaClases.setBounds(40, 40, 140, 430);
		listaClases.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent e) 
			{
				int index = listaClases.getSelectedIndex();

				if (listaClases.getSelectedValue() != null)
				{
					String nombreClase = listaClases.getSelectedValue();
					
					int pos = -1;
					for (int i = 0; i < clases.size(); i++)
					{
						if (clases.get(i).getNombre().equals(nombreClase))
						{
							pos = i;
							break;
						}
					}
					
					Clase miClase = null;
					claseActual = pos;
					
					if (pos != -1)
					{
						miClase = clases.get(pos);
						panelClase.setVisible(true);
						panelAtributo.setVisible(false);
						listaAtributos.setSelectedIndex(-1);
						atributoActual = -1;
						
						lblNombreClase.setText("Clase: " + listaClases.getSelectedValue());
						
						if (miClase.isRelacion())
						{
							checkRelacion.setSelected(true);
						}
						else
						{
							checkRelacion.setSelected(false);
						}
					}
				}
				else
				{
					panelClase.setVisible(false);
				}
				
				actualizar();
				listaClases.setSelectedIndex(index);
			}
		});
		panel.add(listaClases);
		
		txtAgregarClase = new JTextField();
		txtAgregarClase.setBounds(40, 480, 140, 20);
		panel.add(txtAgregarClase);
		txtAgregarClase.setColumns(10);
		
		JButton btnAgregarClase = new JButton("Agregar Clase");
		btnAgregarClase.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (!txtAgregarClase.getText().trim().equals(""))
				{
					String nombreClase = txtAgregarClase.getText().trim();
					nombreClase = nombreClase.replace(" ", "_");
					nombreClase = nombreClase.replace("\t", "_");
					nombreClase = nombreClase.replace("\n", "_");
					
					Clase claseTemp = new Clase();
					claseTemp.setNombre(nombreClase);
					
					int pos = -1;
					for (int i = 0; i < clases.size(); i++)
					{
						if (clases.get(i).getNombre().equals(nombreClase))
						{
							pos = i;
							break;
						}
					}

					if (pos == -1)
					{
						txtAgregarClase.setText("");
						
						if (clases.isEmpty())
						{
							claseTemp.setId(1);
						}
						else
						{
							claseTemp.setId(clases.getLast().getId()+1);
						}

						clases.add(claseTemp);

						actualizar();
					}
				}
			}
		});
		btnAgregarClase.setBounds(40, 510, 140, 25);
		panel.add(btnAgregarClase);

		JButton btnQuitarClase = new JButton("Quitar Clase");
		btnQuitarClase.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String nombreClase = listaClases.getSelectedValue();
				
				int pos = -1;
				for (int i = 0; i < clases.size(); i++)
				{
					if (clases.get(i).getNombre().equals(nombreClase))
					{
						pos = i;
						break;
					}
				}
								
				if (pos != -1)
				{
					if (pos == claseActual)
					{
						claseActual = -1;
						atributoActual = -1;
						panelClase.setVisible(false);
					}
					
					clases.remove(pos);
				}
				
				actualizar();
			}
		});
		btnQuitarClase.setBounds(40, 540, 140, 25);
		panel.add(btnQuitarClase);

		panelClase = new JPanel();
		panelClase.setBackground(SystemColor.controlHighlight);
		panelClase.setBounds(200, 40, 570, 530);
		panel.add(panelClase);
		panelClase.setLayout(null);
		panelClase.setVisible(false);
		
		lblNombreClase = new JLabel("Nombre Clase");
		lblNombreClase.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombreClase.setBounds(0, 5, 570, 15);
		panelClase.add(lblNombreClase);

		JLabel lblAtributos = new JLabel("Atributos");
		lblAtributos.setHorizontalAlignment(SwingConstants.CENTER);
		lblAtributos.setBounds(5, 30, 155, 25);
		panelClase.add(lblAtributos);

		checkRelacion = new JCheckBox("¿Es Relacion?");
		checkRelacion.setBackground(SystemColor.controlHighlight);
		checkRelacion.setHorizontalAlignment(SwingConstants.CENTER);
		checkRelacion.setBounds(177, 30, 160, 25);
		panelClase.add(checkRelacion);
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (claseActual != -1)
				{
					Clase miClase = clases.get(claseActual);

					if (checkRelacion.isSelected())
					{
						miClase.setRelacion(true);
					}
					else
					{
						miClase.setRelacion(false);
					}
				}
			}
		});
		btnGuardar.setBounds(354, 30, 160, 25);
		panelClase.add(btnGuardar);
		
		listaAtributos = new JList<String>();
		listaAtributos.setBackground(SystemColor.controlLtHighlight);
		listaAtributos.setBounds(10, 60, 140, 370);
		listaAtributos.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent e) 
			{
				if (claseActual != -1)
				{
					Clase miClase = clases.get(claseActual);
					
					int pos = -1;
					for (int i = 0; i < miClase.getAtributos().size(); i++)
					{
						if (miClase.getAtributos().get(i).getNombre().equals(listaAtributos.getSelectedValue()))
						{
							pos = i;
						}
					}
					
					if (pos != -1)
					{
						AtributoClase atributo = miClase.getAtributos().get(pos);
						atributoActual = pos;
						panelAtributo.setVisible(true);
						
						txtNombreAtributo.setText(atributo.getNombre());
						txtNombreAtributo.setEnabled(false);
						comboTipo.setSelectedIndex(atributo.getTipo());
						txtDefecto.setText(atributo.getDefecto());
						txtDescripcion.setText(atributo.getDescripcion());
						checkPrimaria.setSelected(atributo.isPrimaria());
						checkForanea.setSelected(atributo.isForanea());
						checkNula.setSelected(atributo.isNula());
						checkDisimil.setSelected(atributo.isDisimil());
					}
				}
			}
		});
		panelClase.add(listaAtributos);
		
		txtAgregarAtributo = new JTextField();
		txtAgregarAtributo.setBounds(10, 440, 140, 20);
		panelClase.add(txtAgregarAtributo);
		txtAgregarAtributo.setColumns(10);
		
		JButton btnAgregarAtributo = new JButton("+ Atributo");
		btnAgregarAtributo.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (!txtAgregarAtributo.getText().trim().equals(""))
				{
					String nombreAtributo = txtAgregarAtributo.getText().trim();
					nombreAtributo = nombreAtributo.replace(" ", "_");
					nombreAtributo = nombreAtributo.replace("\t", "_");
					nombreAtributo = nombreAtributo.replace("\n", "_");
					
					AtributoClase atributoTemp = new AtributoClase();
					atributoTemp.setNombre(nombreAtributo);

					if (claseActual != -1)
					{
						Clase miClase = clases.get(claseActual);
						
						int pos = -1;
						for (int i = 0; i < miClase.getAtributos().size(); i++)
						{
							if (miClase.getAtributos().get(i).equals(nombreAtributo))
							{
								pos = i;
							}
						}
						
						if (pos == -1)
						{							
							if (miClase.getAtributos().isEmpty())
							{
								atributoTemp.setId(1);
							}
							else
							{
								atributoTemp.setId(miClase.getAtributos().getLast().getId()+1);
							}
							
							miClase.getAtributos().add(atributoTemp);

							txtAgregarAtributo.setText("");
							actualizar();
						}
					}
				}
			}
		});
		btnAgregarAtributo.setBounds(10, 470, 140, 25);
		panelClase.add(btnAgregarAtributo);

		JButton btnQuitarAtributo = new JButton("- Atributo");
		btnQuitarAtributo.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{				
				if (claseActual != -1 && atributoActual != -1)
				{
					String nombreAtributo = listaAtributos.getSelectedValue();
					Clase miClase = clases.get(claseActual);
					
					int pos = -1;
					for (int i = 0; i < miClase.getAtributos().size(); i++)
					{
						if (miClase.getAtributos().get(i).getNombre().equals(nombreAtributo))
						{
							pos = i;
							break;
						}
					}
									
					System.out.println(claseActual);
					System.out.println(atributoActual);
					System.out.println(pos);

					if (pos != -1)
					{
						if (pos == atributoActual)
						{
							atributoActual = -1;
							panelAtributo.setVisible(false);
						}
						
						miClase.getAtributos().remove(pos);
					}
					
					actualizar();
				}
			}
		});
		btnQuitarAtributo.setBounds(10, 500, 140, 25);
		panelClase.add(btnQuitarAtributo);
		
		panelAtributo = new JPanel();
		panelAtributo.setBounds(160, 60, 400, 465);
		panelClase.add(panelAtributo);
		panelAtributo.setBackground(Color.WHITE);
		panelAtributo.setLayout(null);
		panelAtributo.setVisible(false);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(20, 50, 90, 20);
		panelAtributo.add(lblNombre);
		
		txtNombreAtributo = new JTextField();
		txtNombreAtributo.setBounds(110, 50, 250, 20);
		panelAtributo.add(txtNombreAtributo);
		txtNombreAtributo.setColumns(10);

		JLabel lblTipo = new JLabel("Tipo");
		lblTipo.setBounds(20, 90, 90, 20);
		panelAtributo.add(lblTipo);
				
		comboTipo = new JComboBox<String>();
		comboTipo.setModel(new DefaultComboBoxModel<String>(new String[] {"INT", "String", "Date", "Booleano", "Enum"}));
		comboTipo.setBounds(110, 90, 250, 20);
		panelAtributo.add(comboTipo);

		JLabel lblDescripcion = new JLabel("Descripcion");
		lblDescripcion.setBounds(20, 130, 90, 20);
		panelAtributo.add(lblDescripcion);
		
		txtDescripcion = new JTextField();
		txtDescripcion.setBounds(110, 130, 250, 20);
		panelAtributo.add(txtDescripcion);
		txtDescripcion.setColumns(10);

		JLabel lblDefecto = new JLabel("Defecto");
		lblDefecto.setBounds(20, 170, 90, 20);
		panelAtributo.add(lblDefecto);
		
		txtDefecto = new JTextField();
		txtDefecto.setBounds(110, 170, 250, 20);
		panelAtributo.add(txtDefecto);
		txtDefecto.setColumns(10);
		
		checkPrimaria = new JCheckBox("¿Es Primaria?");
		checkPrimaria.setBackground(Color.WHITE);
		checkPrimaria.setHorizontalAlignment(SwingConstants.CENTER);
		checkPrimaria.setBounds(20, 230, 340, 20);
		panelAtributo.add(checkPrimaria);		

		checkForanea = new JCheckBox("¿Es Foranea?");
		checkForanea.setBackground(Color.WHITE);
		checkForanea.setHorizontalAlignment(SwingConstants.CENTER);
		checkForanea.setBounds(20, 280, 340, 20);
		panelAtributo.add(checkForanea);		

		checkDisimil = new JCheckBox("¿Es Disimil?");
		checkDisimil.setBackground(Color.WHITE);
		checkDisimil.setHorizontalAlignment(SwingConstants.CENTER);
		checkDisimil.setBounds(20, 330, 340, 20);
		panelAtributo.add(checkDisimil);		

		checkNula = new JCheckBox("¿Es Nula?");
		checkNula.setBackground(Color.WHITE);
		checkNula.setHorizontalAlignment(SwingConstants.CENTER);
		checkNula.setBounds(20, 380, 340, 20);
		panelAtributo.add(checkNula);		
		
		JButton btnGuardarAtributo = new JButton("Guardar");
		btnGuardarAtributo.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (atributoActual != -1 && claseActual != -1)
				{
					AtributoClase atributo = clases.get(claseActual).getAtributos().get(atributoActual);
					
					atributo.setTipo(comboTipo.getSelectedIndex());
					atributo.setDefecto(txtDefecto.getText());
					atributo.setDescripcion(txtDescripcion.getText());
					atributo.setPrimaria(checkPrimaria.isSelected());
					atributo.setForanea(checkForanea.isSelected());
					atributo.setNula(checkNula.isSelected());
					atributo.setDisimil(checkDisimil.isSelected());
										
					actualizar();
				}
			}
		});
		btnGuardarAtributo.setBounds(60, 420, 270, 25);
		panelAtributo.add(btnGuardarAtributo);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 800, 20);
		panel.add(menuBar);
		
		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmNuevo = new JMenuItem("Nuevo");
		mntmNuevo.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				clases = new LinkedList<Clase>();
				claseActual = -1;
				atributoActual = -1;
				actualizar();
				panelClase.setVisible(false);
			}
		});
		mnArchivo.add(mntmNuevo);
		
		JMenuItem mntmLeer = new JMenuItem("Leer");
		mntmLeer.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				//Creamos la estructura para abrir archivos
				 FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos GX", "gx");
				 chooser.setCurrentDirectory(new java.io.File("."));
				 chooser.setDialogTitle("Leer GX");
				 chooser.setApproveButtonText("Leer");
				 chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				 chooser.setFileFilter(filter);
				 
				 if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
				 {					 
					 //Leemos el archivo csv					 
					 if (chooser.getSelectedFile().getAbsolutePath().endsWith(".gx"))
					 {
						 clases = ManejadorArchivos.importar(chooser.getSelectedFile().getAbsolutePath());
						 claseActual = -1;
						 atributoActual = -1;
						 actualizar();
						 panelClase.setVisible(false);						 
					 }
				 } 
			}
		});
		mnArchivo.add(mntmLeer);
				
		JMenuItem mntmGuardar = new JMenuItem("Guardar");
		mntmGuardar.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				if (clases != null && !clases.isEmpty())
				{
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Guardar GX");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setApproveButtonText("Guardar");
					 
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
					{				 				
						 String archivo = chooser.getSelectedFile().getAbsolutePath();
						 					 
						 if (!archivo.endsWith(".gx"))
						 {
							 archivo += ".gx";
						 }
						 
						 if (archivo.endsWith(".gx"))
						 {
							 ManejadorArchivos.exportar(archivo, clases);
						 }						 
					}
				}
			}
		});
		mnArchivo.add(mntmGuardar);
		
		JMenuItem mntmExportarPhp = new JMenuItem("Exportar PHP");
		mntmExportarPhp.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				for (int i = 0; i < clases.size(); i++)
				{
					ExportadorPHP.exportarClase(clases.get(i));
					ExportadorPHP.exportarControlador(clases.get(i));
				}
			}
		});
		mnArchivo.add(mntmExportarPhp);
	}
}
