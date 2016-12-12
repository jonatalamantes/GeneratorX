import java.util.LinkedList;

public class AtributoClase 
{
	/**
	 * Atributo Identificador de la clase
	 */
	private int id;
	
	/**
	 * Nombre de la clase
	 */
	private String nombre;
	
	/**
	 * Tipo de datos que puede tener 
	 * 0 - INT
	 * 1 - String
	 * 2 - Date
	 * 3 - Booleano
	 * 4 - Enum
	 */
	private int tipo;
	
	/**
	 * Descripcion de ser necesaria del atributo
	 */
	private String descripcion;
	
	/**
	 * Atributo si indica si será primaria o no
	 */
	private boolean primaria;
	
	/**
	 * Atributo que define si es una llave foranea
	 */
	private boolean foranea;
	
	/**
	 * Atributo que define si puede ser nulo
	 */
	private boolean nula;
	
	/**
	 * Atrubuto que define si es necesario este atributo para la disimilitud
	 */
	private boolean disimil;
	
	/**
	 * Atributo que define el valor que tendra por defecto
	 */
	private String defecto;
	
	private LinkedList<String> prefijos; 
	
	AtributoClase()
	{
		this.id = 0;
		this.nombre = "";
		this.tipo = 0;
		this.descripcion = "";
		this.primaria = false;
		this.foranea = false;
		this.nula = false;
		this.disimil = false;
		this.defecto = "";
		this.prefijos = new LinkedList<String>();
		
		this.prefijos.add("tcat_");
		this.prefijos.add("t");
	}

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public String getNombre() 
	{
		return this.nombre;
	}

	public String getNombreNP() 
	{
		if (this.isForanea())
		{
			String nombreTemp = "";
			String nombreAttr = this.nombre;
			String nombrePrefijo = "";
			
			for (int i = 0; i < this.prefijos.size(); i++)
			{
				nombrePrefijo = this.prefijos.get(i);
				
				System.out.println(nombreAttr+ " "+ nombrePrefijo);
				
				if (nombreAttr.length() > nombrePrefijo.length())
				{
					nombreTemp = nombreAttr.substring(0, nombrePrefijo.length());
					
					System.out.println(nombreTemp);
					
					if (nombreTemp.equals(nombrePrefijo))
					{
						nombreTemp = nombreAttr.substring(nombreTemp.length()); 
						System.out.println("Retornando: " + nombreTemp);
						return nombreTemp;
					}
				}
				else
				{
					continue;
				}
			}
		}

		return this.nombre;
	}

	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}

	public int getTipo() 
	{
		return tipo;
	}

	public void setTipo(int i) 
	{
		this.tipo = i;
	}

	public String getDescripcion() 
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion) 
	{
		this.descripcion = descripcion;
	}

	public boolean isPrimaria() 
	{
		return primaria;
	}

	public void setPrimaria(boolean primaria) 
	{
		this.primaria = primaria;
	}

	public boolean isForanea() 
	{
		return foranea;
	}

	public void setForanea(boolean foranea) 
	{
		this.foranea = foranea;
	}

	public boolean isNula() 
	{
		return nula;
	}

	public void setNula(boolean nula) 
	{
		this.nula = nula;
	}

	public boolean isDisimil() 
	{
		return disimil;
	}

	public void setDisimil(boolean disimil) 
	{
		this.disimil = disimil;
	}

	public String getDefecto() 
	{
		return defecto;
	}

	public void setDefecto(String defecto) 
	{
		this.defecto = defecto;
	}
	
	public String toString()
	{
		return this.nombre;
	}
}
