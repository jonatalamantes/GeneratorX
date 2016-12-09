import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ExportadorPHP 
{
	public static String toCamelCase(String cad)
	{
		String cadNueva = "";
		boolean mayuscula = false;
		
		for (int i = 0; i < cad.length(); i++)
		{
			if (cad.charAt(i) == '_')
			{
				mayuscula = true;
			}
			else
			{
				String temp = "";
				temp += cad.charAt(i);

				if (mayuscula)
				{
					
					cadNueva += temp.toUpperCase();
					mayuscula = false;
				}
				else
				{
					cadNueva += temp.toLowerCase();
				}
			}
		}
		
		return cadNueva;
	}

	public static String toCapitalCase(String cad)
	{
		String primera = cad.substring(0, 1).toUpperCase();
		String segunda = toCamelCase(cad.substring(1));
				
		return primera + segunda;
	}
	
	public static void imprimir(String msj, PrintWriter writer, int cantidad)
	{
		for (int i = 0; i < cantidad; i++)
		{
			writer.print("    ");
		}
		
		writer.println(msj);
	}
	
	public static int getTamEspacios(String comparar, Clase clase, boolean camel)
	{
		int valMayor = -1;
		
		for (int i = 0; i < clase.getAtributos().size(); i++)
		{
			String nombreClase = clase.getAtributos().get(i).getNombre();
			
			if (camel)
			{
				nombreClase = toCamelCase(nombreClase);
			}
			
			if (nombreClase.length() > valMayor)
			{
				valMayor = nombreClase.length();
			}
		}
		
		if (camel)
		{
			return Math.abs(toCamelCase(comparar).length() - valMayor);
		}
		else
		{
			return Math.abs(comparar.length() - valMayor);
		}
		
	}

	public static void exportarClase(Clase clase)
	{
		String nombreClase = toCapitalCase(clase.getNombre());
		String path = "code/" + nombreClase + ".php";
		PrintWriter writer;
		int espacioTabulador = 0;
		
		try 
		{
			writer = new PrintWriter(path, "UTF-8");

			//Creamos la parte de arriba de la clase
			writer.println("<?php ");
			writer.println();
			espacioTabulador++;
			imprimir("/**", writer, espacioTabulador);
			imprimir("* Clase para el " + nombreClase, writer, espacioTabulador);
			imprimir("*/", writer, espacioTabulador);
			imprimir("Class " + nombreClase, writer, espacioTabulador);
			imprimir("{", writer, espacioTabulador);
			espacioTabulador++;
			
			//Creamos los atributos
			for (int i = 0; i < clase.getAtributos().size(); i++)
			{
				imprimir("private $" + toCamelCase(clase.getAtributos().get(i).getNombre()) + ";", writer, espacioTabulador);
			}
			
			writer.println("");
			
			//Creamos el constructor
			imprimir("/**", writer, espacioTabulador);
			imprimir(" * Constructor de la Clase", writer, espacioTabulador);
			imprimir(" */", writer, espacioTabulador);
			
			String cadAcumulada = "function __construct(";
			boolean primero = false;
			for (int i = 0; i < clase.getAtributos().size(); i++)
			{
				AtributoClase atributoActual = clase.getAtributos().get(i);
				String defecto = "\"\"";
				String defectoPrev = atributoActual.getDefecto().replaceAll("\"", "");
				
				if (defectoPrev.equals(""))
				{
					defecto = "\""+defectoPrev+"\"";
				}
				
				if (i == 0)
				{
					primero = true;
					cadAcumulada += "$" + atributoActual.getNombre() + " = " + defecto + ", "; 
				}
				else if (i % 3 == 0)
				{
					if (i == clase.getAtributos().size()-1)
					{
						cadAcumulada = cadAcumulada.substring(0, cadAcumulada.length()-2);
						cadAcumulada += ")";
					}
					
					if (primero)
					{
						primero = false;
						imprimir(cadAcumulada, writer, espacioTabulador);
					}
					else
					{
						imprimir(cadAcumulada, writer, espacioTabulador+5);
					}		
					
					cadAcumulada = "";
					cadAcumulada += "$" + atributoActual.getNombre() + " = " + defecto + ", ";
				}
				else
				{
					cadAcumulada += "$" + atributoActual.getNombre() + " = " + defecto + ", ";
				}
			}
			
			if (!cadAcumulada.equals(""))
			{
				cadAcumulada = cadAcumulada.substring(0, cadAcumulada.length()-2);
				
				if (primero)
				{
					primero = false;
					imprimir(cadAcumulada + ")", writer, espacioTabulador);
				}
				else
				{
					imprimir(cadAcumulada + ")", writer, espacioTabulador+5);
				}					
			}
			
			imprimir ("{", writer, espacioTabulador);
			
			for (int i = 0; i < clase.getAtributos().size(); i++)
			{
				String cad = "$this->" + toCamelCase(clase.getAtributos().get(i).getNombre());
				int tam = getTamEspacios(clase.getAtributos().get(i).getNombre(), clase, true);
				
				for (int j = 0; j < tam; j++)
				{
					cad += " ";
				}
				
				cad += " = $" + clase.getAtributos().get(i).getNombre() + ";";  
				
				imprimir(cad, writer, espacioTabulador+1);
			}
			
			imprimir("}", writer, espacioTabulador);
			imprimir("", writer, espacioTabulador);
			
			//Generamos el metodo de ToArray
	        imprimir("/**", writer, espacioTabulador);
	        imprimir(" * Retorna un Array del Objeto", writer, espacioTabulador);
	        imprimir(" * ", writer, espacioTabulador);
	        imprimir(" * @return [array] [Array Asociativo Resultante]", writer, espacioTabulador);
	        imprimir(" */", writer, espacioTabulador);
	        imprimir("public function toArray()", writer, espacioTabulador);
	        imprimir("{", writer, espacioTabulador);
	        imprimir("$array = array();", writer, espacioTabulador+1);
	        imprimir("", writer, espacioTabulador);
	        imprimir("if ($this !== null)", writer, espacioTabulador+1);
	        imprimir("{", writer, espacioTabulador+1);
			for (int i = 0; i < clase.getAtributos().size(); i++)
			{
				String cad = "$array[\"" + clase.getAtributos().get(i).getNombre() + "\"]";
				int tam = getTamEspacios(clase.getAtributos().get(i).getNombre(), clase, false);
				
				for (int j = 0; j < tam; j++)
				{
					cad += " ";
				}
				
				cad += " = $this->" + toCamelCase("get_" + clase.getAtributos().get(i).getNombre()) + "();";  
				
				imprimir(cad, writer, espacioTabulador+2);
			}
			imprimir("}", writer, espacioTabulador+1);
			imprimir("", writer, espacioTabulador);
			imprimir("return $array;", writer, espacioTabulador+1);
			imprimir("}", writer, espacioTabulador);
			imprimir("", writer, espacioTabulador);
			
			//Creamos el metodo fromArray
			imprimir("/**", writer, espacioTabulador);
			imprimir(" * Toma los datos de un Array para el Objeto", writer, espacioTabulador);
			imprimir(" * ", writer, espacioTabulador);
			imprimir(" * @param  array  $array [Array Entrante]", writer, espacioTabulador);
			imprimir(" */", writer, espacioTabulador);
			imprimir("public function fromArray($array = array())", writer, espacioTabulador);
			imprimir("{", writer, espacioTabulador);
			imprimir("if (!empty($array))", writer, espacioTabulador+1);
			imprimir("{", writer, espacioTabulador+1);
			for (int i = 0; i < clase.getAtributos().size(); i++)
			{
				String cad = "$this->" + toCamelCase("set_" + clase.getAtributos().get(i).getNombre()) + "(";
				cad += "$array[\"" + clase.getAtributos().get(i).getNombre() + "\"]);";  
				
				imprimir(cad, writer, espacioTabulador+2);
			}
			imprimir("}", writer, espacioTabulador+1);
			imprimir("}", writer, espacioTabulador);
			imprimir("", writer, espacioTabulador);
			
			//Creamos el metodo de la disimilitud
			imprimir("/**", writer, espacioTabulador);
			imprimir(" * Calculo para saber que tan diferente es un objeto de otro", writer, espacioTabulador);
			imprimir(" * ", writer, espacioTabulador);
			imprimir(" * @param  Alumno $obj [Objeto con el que se comparara]", writer, espacioTabulador);
			imprimir(" * @return [float]     [Disimilitud entre los dos objetos]", writer, espacioTabulador);
			imprimir(" */", writer, espacioTabulador);
			imprimir("public function disimilitud($obj = null)", writer, espacioTabulador);
			imprimir("{", writer, espacioTabulador);
			imprimir("if ($obj === null)", writer, espacioTabulador+1);
			imprimir("{", writer, espacioTabulador+1);
			imprimir("return -1;", writer, espacioTabulador+2);
			imprimir("}", writer, espacioTabulador+1);
			imprimir("", writer, espacioTabulador);
			imprimir("$disimilitud = 0;", writer, espacioTabulador+1);
			imprimir("$numerador   = 0;", writer, espacioTabulador+1);
			imprimir("$denominador = 0;", writer, espacioTabulador+1);
			imprimir("", writer, espacioTabulador+1);
			for (int i = 0; i < clase.getAtributos().size(); i++)
			{
				if (clase.getAtributos().get(i).isDisimil())
				{
					String getter = toCamelCase("get_" + clase.getAtributos().get(i).getNombre());
					
					imprimir("if ($obj->" + getter + "() != $this->" + getter + "())", writer, espacioTabulador+1);
					imprimir("{", writer, espacioTabulador+1);
					imprimir("$numerador += 1;", writer, espacioTabulador+2);
					imprimir("}", writer, espacioTabulador+1);
					imprimir("", writer, espacioTabulador);
					imprimir("$denominador += 1;", writer, espacioTabulador+1);
					imprimir("", writer, espacioTabulador);
				}
			}
            imprimir("$disimilitud = (float)($numerador/$denominador);", writer, espacioTabulador+1);
            imprimir("return $disimilitud;", writer, espacioTabulador+1);
            imprimir("}", writer, espacioTabulador);
            imprimir("", writer, espacioTabulador);

            //Creamos los getters y setters
            for (int i = 0; i < clase.getAtributos().size(); i++)
            {
            	String nombre = toCamelCase(clase.getAtributos().get(i).getNombre());
            	String nombreGetter = toCamelCase("get_" + clase.getAtributos().get(i).getNombre());
            	String nombreSetter = toCamelCase("set_" + clase.getAtributos().get(i).getNombre());
            	
            	imprimir("/**", writer, espacioTabulador);
            	imprimir(" * Gets the value of " + nombre, writer, espacioTabulador);
            	imprimir(" */", writer, espacioTabulador);
            	imprimir("public function " + nombreGetter +"()", writer, espacioTabulador);
            	imprimir("{", writer, espacioTabulador);
            	imprimir("return $this->" + nombre + ";", writer, espacioTabulador+1);
            	imprimir("}", writer, espacioTabulador);
            	imprimir("", writer, espacioTabulador);
            	imprimir("/**", writer, espacioTabulador);
            	imprimir(" * Sets the value of " + nombre, writer, espacioTabulador);
            	imprimir(" */", writer, espacioTabulador);
            	imprimir("public function " + nombreSetter + "($" + nombre + ")", writer, espacioTabulador);
            	imprimir("{", writer, espacioTabulador);
            	imprimir("$this->" + nombre + " = $" + nombre + ";", writer, espacioTabulador+1);
            	imprimir("}", writer, espacioTabulador);
            	imprimir("", writer, espacioTabulador);
            }
            
            imprimir("}", writer, espacioTabulador-1);
			writer.println("?>");
			
			writer.close();
		}
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		} 
		catch (UnsupportedEncodingException e1) 
		{
			e1.printStackTrace();
		}

	}
	
	public static void exportarControlador(Clase clase)
	{
		
	}
}
