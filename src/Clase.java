import java.util.LinkedList;

public class Clase 
{
	private int id;
	private String nombre;
	private LinkedList<AtributoClase> atributos;
	private boolean relacion;
	
	Clase()
	{
		this.id = 0;
		this.nombre = "";
		this.atributos = new LinkedList<AtributoClase>();
		this.relacion = false;
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
		return nombre;
	}

	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}

	public LinkedList<AtributoClase> getAtributos() 
	{
		return atributos;
	}

	public void setAtributos(LinkedList<AtributoClase> atributos) 
	{
		this.atributos = atributos;
	}

	public boolean isRelacion() 
	{
		return relacion;
	}

	public void setRelacion(boolean relacion) 
	{
		this.relacion = relacion;
	}	
	
	public String toString()
	{
		return this.nombre;
	}
	
	public AtributoClase getPrimaria()
	{
		for (int i = 0; i < this.getAtributos().size(); i++)
		{
			if (this.getAtributos().get(i).isPrimaria())
			{
				return this.getAtributos().get(i);
			}
		}
		
		return null;
	}
}
