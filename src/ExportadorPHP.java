import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

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
			imprimir(" * @param  " + nombreClase + " $obj [Objeto con el que se comparara]", writer, espacioTabulador);
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
		String nombreClase = toCapitalCase(clase.getNombre());
		String path = "code/Controlador" + nombreClase + ".php";
		PrintWriter writer;
		int espacioTabulador = 0;
		String tabla = "TABLE_" + nombreClase.toUpperCase();
		
		try 
		{
			writer = new PrintWriter(path, "UTF-8");

			/* Creamos la parte de arriba de la clase */
			writer.println("<?php ");
			writer.println();
			espacioTabulador++;
			
			imprimir("require_once(__DIR__.\"/" + nombreClase + ".php\");", writer, espacioTabulador);
			imprimir("require_once(__DIR__.\"/DatabaseManager.php\");", writer, espacioTabulador);
			imprimir("", writer, espacioTabulador);
		    imprimir("/**", writer, espacioTabulador);
		    imprimir(" * Clase para Manipular Objetos del Tipo " + nombreClase, writer, espacioTabulador);
		    imprimir(" */", writer, espacioTabulador);
		    imprimir("class Controlador" + nombreClase + "", writer, espacioTabulador);
		    imprimir("{", writer, espacioTabulador);
		    espacioTabulador++;

		    /* Creamos el metodo de getSingle */
		    imprimir("/**", writer, espacioTabulador);
		    imprimir(" * Recupera un objeto de tipo " + nombreClase, writer, espacioTabulador);
		    imprimir(" */", writer, espacioTabulador);
		    imprimir("static function getSingle($keysValues = array())", writer, espacioTabulador);
		    imprimir("{", writer, espacioTabulador);
		    imprimir("if (!is_array($keysValues) || empty($keysValues))", writer, espacioTabulador+1);
		    imprimir("{", writer, espacioTabulador+1);
		    imprimir("return null;", writer, espacioTabulador+2);
		    imprimir("}", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador);
		    imprimir("$table" + nombreClase + "  = DatabaseManager::getNameTable('" + tabla + "');", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador);
		    imprimir("$query     = \"SELECT $table" + nombreClase + ".*", writer, espacioTabulador+1);
		    imprimir("              FROM $table" + nombreClase, writer, espacioTabulador+1);
		    imprimir("              WHERE \";", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador);		    
		    imprimir("foreach ($keysValues as $key => $value)", writer, espacioTabulador+1);
		    imprimir("{", writer, espacioTabulador+1);
		    imprimir("$query .= \"$table" + nombreClase + ".$key = '$value' AND \";", writer, espacioTabulador+2);
		    imprimir("}", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador+1);
		    imprimir("$query = substr($query, 0, strlen($query)-4);", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador+1);
		    imprimir("$" + nombreClase.toLowerCase() + "_simple  = DatabaseManager::singleFetchAssoc($query);" , writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador+1);
		    imprimir("if ($" + nombreClase.toLowerCase() + "_simple !== NULL)", writer, espacioTabulador+1);
		    imprimir("{", writer, espacioTabulador+1);
		    imprimir("$" + nombreClase.toLowerCase() + "A = new " + nombreClase + "();", writer, espacioTabulador+2);
		    imprimir("$" + nombreClase.toLowerCase() + "A->fromArray($" + nombreClase.toLowerCase() + "_simple);", writer, espacioTabulador+2);
		    imprimir("}", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador);
		    imprimir("return $" + nombreClase.toLowerCase() + "A;", writer, espacioTabulador+1);
		    imprimir("}", writer, espacioTabulador);		    
	        imprimir("", writer, espacioTabulador);

		    /* Creamos el metodo de getAll */
		    imprimir("/**", writer, espacioTabulador);
		    imprimir(" * Obtiene todos los " + nombreClase.toLowerCase() + "s de la tabla de " + nombreClase.toLowerCase() + "s", writer, espacioTabulador);
		    imprimir(" */", writer, espacioTabulador);
		    imprimir("static function getAll($order = 'id', $begin = 0, $cantidad = 10)", writer, espacioTabulador);
		    imprimir("{", writer, espacioTabulador);
			imprimir("$table" + nombreClase + "  = DatabaseManager::getNameTable('" + tabla + "');", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador);
		    imprimir("$query     = \"SELECT $table" + nombreClase + ".*", writer, espacioTabulador+1);
		    imprimir("              FROM $table" + nombreClase + "", writer, espacioTabulador+1);
		    imprimir("              WHERE $table" + nombreClase + ".activo = 'S'", writer, espacioTabulador+1);
		    imprimir("              ORDER BY \";", writer, espacioTabulador+1);
	        imprimir("", writer, espacioTabulador);
	        
	        //Creanos los if else de los ordenamientos
	        boolean primera = true;
	        int posicionPrimaria = -1;
	        for (int i = 0; i < clase.getAtributos().size(); i++)
	        {
	        	if (clase.getAtributos().get(i).isDisimil() && !clase.getAtributos().get(i).isPrimaria())
	        	{
	        		String nombreAtributo = clase.getAtributos().get(i).getNombre();
	        		
	        		if (primera)
	        		{
	        			imprimir("if ($order == '" + toCamelCase(nombreAtributo) + "')", writer, espacioTabulador+1);
	        			imprimir("{", writer, espacioTabulador+1);
	        			imprimir("$query = $query . \" $table" + nombreClase + "." + nombreAtributo + "\";", writer, espacioTabulador+2);
	        			imprimir("}", writer, espacioTabulador+1);
	        			primera = false;
	        		}
	        		else
	        		{
	        			imprimir("else if ($order == '" + toCamelCase(nombreAtributo) + "')", writer, espacioTabulador+1);
	        			imprimir("{", writer, espacioTabulador+1);
	        			imprimir("$query = $query . \" $table" + nombreClase + "." + nombreAtributo + "\";", writer, espacioTabulador+2);
	        			imprimir("}", writer, espacioTabulador+1);
	        		}
	        	}
	        	else if (clase.getAtributos().get(i).isPrimaria())
	        	{
	        		posicionPrimaria = i;
	        	}
	        }
	        
	        if (posicionPrimaria != -1)
	        {
        		String nombreAtributo = clase.getAtributos().get(posicionPrimaria).getNombre();
    			imprimir("else", writer, espacioTabulador+1);
    			imprimir("{", writer, espacioTabulador+1);
    			imprimir("$query = $query . \" $table" + nombreClase + "." + nombreAtributo + " DESC\";", writer, espacioTabulador+2);
    			imprimir("}", writer, espacioTabulador+1);
	        }
	        
			imprimir("", writer, espacioTabulador);
			imprimir("if ($begin >= 0)", writer, espacioTabulador+1);
			imprimir("{", writer, espacioTabulador+1);
			imprimir("$query = $query. \" LIMIT \" . strval($begin * $cantidad) . \", \" . strval($cantidad+1);", writer, espacioTabulador+2);
			imprimir("}", writer, espacioTabulador+1);
			imprimir("", writer, espacioTabulador);
			imprimir("$array" + nombreClase + "s   = DatabaseManager::multiFetchAssoc($query);", writer, espacioTabulador+1);
			imprimir("$" + nombreClase.toLowerCase() + "_simples = array();", writer, espacioTabulador+1);
			imprimir("", writer, espacioTabulador);
			imprimir("if ($array" + nombreClase + "s !== NULL)", writer, espacioTabulador+1);
			imprimir("{", writer, espacioTabulador+1);
			imprimir("$i = 0;", writer, espacioTabulador+2);
			imprimir("foreach ($array" + nombreClase + "s as $" + nombreClase.toLowerCase() + "_simple) ", writer, espacioTabulador+2);
			imprimir("{", writer, espacioTabulador+2);
			imprimir("if ($i == $cantidad && $begin >= 0)", writer, espacioTabulador+3);
			imprimir("{", writer, espacioTabulador+3);
			imprimir("continue;", writer, espacioTabulador+4);
			imprimir("}", writer, espacioTabulador+3);
			imprimir("", writer, espacioTabulador);
			imprimir("$" + nombreClase.toLowerCase() + "A = new " + nombreClase + "();", writer, espacioTabulador+3);
			imprimir("$" + nombreClase.toLowerCase() + "A->fromArray($" + nombreClase.toLowerCase() + "_simple);", writer, espacioTabulador+3);
			imprimir("$" + nombreClase.toLowerCase() + "_simples[] = $" + nombreClase.toLowerCase() + "A;", writer, espacioTabulador+3);
			imprimir("$i++;", writer, espacioTabulador+3);
			imprimir("}", writer, espacioTabulador+2);
	    	imprimir("", writer, espacioTabulador);
	    	imprimir("return $" + nombreClase.toLowerCase() + "_simples;", writer, espacioTabulador+2);
	    	imprimir("}", writer, espacioTabulador+1);
	    	imprimir("else", writer, espacioTabulador+1);
	    	imprimir("{", writer, espacioTabulador+1);
	    	imprimir("return NULL;", writer, espacioTabulador+2);
	    	imprimir("}", writer, espacioTabulador+1);
	    	imprimir("}", writer, espacioTabulador);
	        imprimir("", writer, espacioTabulador);

	    	/* Creamos el metodo Add */
	    	imprimir("/**", writer, espacioTabulador);
	    	imprimir(" * Inserta un elemento en la base de datos del tipo " + nombreClase.toLowerCase() + "", writer, espacioTabulador);
	    	imprimir(" */", writer, espacioTabulador);
	    	imprimir("static function add($" + nombreClase.toLowerCase() + " = null)", writer, espacioTabulador);
	    	imprimir("{", writer, espacioTabulador);
	    	imprimir("if ($" + nombreClase.toLowerCase() + " === null)", writer, espacioTabulador+1);
	    	imprimir("{", writer, espacioTabulador+1);
	    	imprimir("return false;", writer, espacioTabulador+2);
	    	imprimir("}", writer, espacioTabulador+1);
	    	imprimir("", writer, espacioTabulador);
	    	
	    	//Generamos el arreglo de opciones
	    	imprimir("$opciones = array(", writer, espacioTabulador+1);
	    	for (int i = 0; i < clase.getAtributos().size(); i++)
	    	{
	    		if (clase.getAtributos().get(i).isDisimil())
	    		{
		    		String nombreAtributo = clase.getAtributos().get(i).getNombre(); 
		    		String cad = "'" + nombreAtributo + "'";
		    		int tam = getTamEspacios(nombreAtributo, clase, false);
		    		for (int j = 0; j < tam; j++)
		    		{
		    			cad += " ";
		    		}
		    		cad += " => $" + nombreClase.toLowerCase() + "->" + toCamelCase("get_" + nombreAtributo) + "(),";
		    		imprimir(cad, writer, espacioTabulador+2);
	    		}	    		
	    	}
    		String nombreAtributo = "activo"; 
    		String cad = "'" + nombreAtributo + "'";
    		int tam = getTamEspacios(nombreAtributo, clase, false);
    		for (int j = 0; j < tam; j++)
    		{
    			cad += " ";
    		}
    		cad += " => 'S'";
    		imprimir(cad, writer, espacioTabulador+2);
    		imprimir(");", writer, espacioTabulador+1);

    		//Continuamos
	    	imprimir("", writer, espacioTabulador);
	    	imprimir("$single" + nombreClase + " = self::getSingle($opciones);", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador);
		    imprimir("if ($single" + nombreClase + " == NULL || $single" + nombreClase + "->disimilitud($" + nombreClase.toLowerCase() + ") == 1)", writer, espacioTabulador+1);
		    imprimir("{", writer, espacioTabulador+1);
	    	
	    	//Generamos las variables de insercion
	    	for (int i = 0; i < clase.getAtributos().size(); i++)
	    	{	    		
	    		nombreAtributo = clase.getAtributos().get(i).getNombre();
	    		
	    		if (nombreAtributo.toLowerCase().equals("activo") || 
	    			toCamelCase(nombreAtributo).toLowerCase().equals("fecharegistro") ||
	    			clase.getAtributos().get(i).isPrimaria())
	    		{
	    			continue;
	    		}
	    		
	    		cad = "$" + nombreAtributo;
	    		tam = getTamEspacios(nombreAtributo, clase, false);
	    		for (int j = 0; j < tam; j++)
	    		{
	    			cad += " ";
	    		}
	    		cad += " = $" + nombreClase.toLowerCase() + "->" + toCamelCase("get_" + nombreAtributo) + "();";
	    		imprimir(cad, writer, espacioTabulador+2);
	    	}
	    	
	    	imprimir("", writer, espacioTabulador);
	        imprimir("$table" + nombreClase + "  = DatabaseManager::getNameTable('" + tabla + "');", writer, espacioTabulador+2);
	    	imprimir("", writer, espacioTabulador);
	    	
	    	//Generamos la consulta
	    	imprimir("$query   = \"INSERT INTO $table" + nombreClase + " ", writer, espacioTabulador+2);
	    	cad = "";
	    	int interno = -1;
	    	for (int i = 0; i < clase.getAtributos().size(); i++)
	    	{
	    		nombreAtributo = clase.getAtributos().get(i).getNombre();
	    		
	    		if (nombreAtributo.toLowerCase().equals("activo") || 
		    		toCamelCase(nombreAtributo).toLowerCase().equals("fecharegistro") ||
		    		clase.getAtributos().get(i).isPrimaria())
	    		{
	    			continue;
	    		}
	    		else
	    		{
	    			interno++;
	    		}

	    		if (interno == 0)
	    		{
	    			cad += "(";
	    			cad += clase.getAtributos().get(i).getNombre() + ", ";
	    		}
	    		else if (interno % 3 == 0)
	    		{					
					if (i == clase.getAtributos().size()-1)
					{
						cad = cad.substring(0, cad.length()-2);
						cad += ")";
					}
					
					imprimir(cad, writer, espacioTabulador+5);
					cad = "";
					cad += clase.getAtributos().get(i).getNombre() + ", ";
	    		}
	    		else
	    		{
	    			cad += clase.getAtributos().get(i).getNombre() + ", ";
	    		}
	    	}
	    	
	    	if (!cad.equals(""))
	    	{
				cad = cad.substring(0, cad.length()-2);
				cad += ")";
				imprimir(cad, writer, espacioTabulador+5);
				cad = "";
	    	}

			imprimir("VALUES", writer, espacioTabulador+5);
	    	cad = "";
	    	interno = -1;
	    	for (int i = 0; i < clase.getAtributos().size(); i++)
	    	{
	    		nombreAtributo = clase.getAtributos().get(i).getNombre();
	    		
	    		if (nombreAtributo.toLowerCase().equals("activo") || 
		    		toCamelCase(nombreAtributo).toLowerCase().equals("fecharegistro") ||
		    		clase.getAtributos().get(i).isPrimaria())	    		
	    		{
	    			continue;
	    		}
	    		else
	    		{
	    			interno++;
	    		}

	    		if (interno == 0)
	    		{
	    			cad += "(";
	    			cad += "'$" + clase.getAtributos().get(i).getNombre() + "', ";
	    		}
	    		else if (interno % 3 == 0)
	    		{    			
					if (i == clase.getAtributos().size()-1)
					{
						cad = cad.substring(0, cad.length()-2);
						cad += ")\";";
					}
					
					imprimir(cad, writer, espacioTabulador+5);
					cad = "";
	    			cad += "'$" + clase.getAtributos().get(i).getNombre() + "', ";
	    		}
	    		else
	    		{
	    			cad += "'$" + clase.getAtributos().get(i).getNombre() + "', ";
	    		}
	    	}
	    	
	    	if (!cad.equals(""))
	    	{
				cad = cad.substring(0, cad.length()-2);
				cad += ")\";";
				imprimir(cad, writer, espacioTabulador+5);
				cad = "";
	    	}
	    	
	    	//Continuamos
	    	imprimir("", writer, espacioTabulador);
	        imprimir("if (DatabaseManager::singleAffectedRow($query) === true)", writer, espacioTabulador+2);
	        imprimir("{", writer, espacioTabulador+2);
	        imprimir("return true;", writer, espacioTabulador+3);
	        imprimir("}", writer, espacioTabulador+2);
	        imprimir("else", writer, espacioTabulador+2);
	        imprimir("{", writer, espacioTabulador+2);
	        imprimir("return false;", writer, espacioTabulador+3);
	        imprimir("}", writer, espacioTabulador+2);
	        imprimir("}", writer, espacioTabulador+1);
	        imprimir("else", writer, espacioTabulador+1);
	        imprimir("{", writer, espacioTabulador+1);
	        imprimir("return false;", writer, espacioTabulador+2);
	        imprimir("}", writer, espacioTabulador+1);
	        imprimir("}", writer, espacioTabulador);
	        imprimir("", writer, espacioTabulador);
	        
	        /* Agregamos el metodo de update */
	        String nombrePrimaria = clase.getPrimaria().getNombre();
	        String getterPrimaria = toCamelCase("get_" + clase.getPrimaria().getNombre()); 
	        
	        imprimir("/**", writer, espacioTabulador);
	        imprimir(" * Actualizar el Contenido de un objeto de tipo " + nombreClase.toLowerCase(), writer, espacioTabulador);
	        imprimir(" */", writer, espacioTabulador);
	        imprimir("static function update($" + nombreClase.toLowerCase() + " = NULL)", writer, espacioTabulador);
	        imprimir("{", writer, espacioTabulador);
	        imprimir("if ($" + nombreClase.toLowerCase() + " === null)", writer, espacioTabulador+1);
	        imprimir("{", writer, espacioTabulador+1);
	        imprimir("return false;", writer, espacioTabulador+2);
	        imprimir("}", writer, espacioTabulador+1);
	        imprimir("", writer, espacioTabulador);
	        imprimir("$opciones = array('"+nombrePrimaria+"' => $" + nombreClase.toLowerCase() + "->" + getterPrimaria + "());", writer, espacioTabulador+1);
	        imprimir("", writer, espacioTabulador);
	        imprimir("$single" + nombreClase + " = self::getSingle($opciones);", writer, espacioTabulador+1);
	        imprimir("", writer, espacioTabulador);
	        imprimir("if ($single" + nombreClase + "->disimilitud($" + nombreClase.toLowerCase() + ") > 0)", writer, espacioTabulador+1);
	        imprimir("{", writer, espacioTabulador+1);

	    	//Generamos las variables de insercion
	    	for (int i = 0; i < clase.getAtributos().size(); i++)
	    	{	    		
	    		nombreAtributo = clase.getAtributos().get(i).getNombre();
	    		
	    		if (nombreAtributo.toLowerCase().equals("activo") || 
	    			toCamelCase(nombreAtributo).toLowerCase().equals("fecharegistro") ||
	    			clase.getAtributos().get(i).isPrimaria())
	    		{
	    			continue;
	    		}
	    		
	    		cad = "$" + nombreAtributo;
	    		tam = getTamEspacios(nombreAtributo, clase, false);
	    		for (int j = 0; j < tam; j++)
	    		{
	    			cad += " ";
	    		}
	    		cad += " = $" + nombreClase.toLowerCase() + "->" + toCamelCase("get_" + nombreAtributo) + "();";
	    		imprimir(cad, writer, espacioTabulador+2);
	    	}
	    	
	    	imprimir("", writer, espacioTabulador);
	        imprimir("$table" + nombreClase + "  = DatabaseManager::getNameTable('" + tabla + "');", writer, espacioTabulador+2);
	    	imprimir("", writer, espacioTabulador);
	    	    	
            //Generamos el arreglo de opciones
	    	imprimir("$opciones = array(", writer, espacioTabulador+2);
	    	for (int i = 0; i < clase.getAtributos().size(); i++)
	    	{
	    		if (clase.getAtributos().get(i).isDisimil())
	    		{
		    		nombreAtributo = clase.getAtributos().get(i).getNombre(); 
		    		cad = "'" + nombreAtributo + "'";
		    		tam = getTamEspacios(nombreAtributo, clase, false);
		    		for (int j = 0; j < tam; j++)
		    		{
		    			cad += " ";
		    		}
		    		cad += " => $" + nombreClase.toLowerCase() + "->" + toCamelCase("get_" + nombreAtributo) + "(),";
		    		imprimir(cad, writer, espacioTabulador+3);
	    		}	    		
	    	}
    		
	    	nombreAtributo = "activo"; 
    		cad = "'" + nombreAtributo + "'";
    		tam = getTamEspacios(nombreAtributo, clase, false);
    		for (int j = 0; j < tam; j++)
    		{
    			cad += " ";
    		}
    		cad += " => 'S'";
    		imprimir(cad, writer, espacioTabulador+3);
    		imprimir(");", writer, espacioTabulador+2);
    		imprimir("", writer, espacioTabulador);

    		//Continuamos
    		imprimir("$single" + nombreClase + " = self::getSingle($opciones);", writer, espacioTabulador+2);
    		imprimir("", writer, espacioTabulador);
    		imprimir("if ($single" + nombreClase + " == NULL || $single" + nombreClase + "->disimilitud($" + nombreClase.toLowerCase() + ") == 1)", writer, espacioTabulador+2);
    		imprimir("{", writer, espacioTabulador+2);
			imprimir("$table" + nombreClase + "  = DatabaseManager::getNameTable('" + tabla + "');", writer, espacioTabulador+3);
    		imprimir("", writer, espacioTabulador);
	                    
    		//Creamos la consulta
    		
    		imprimir("$query =   \"UPDATE $table" + nombreClase, writer, espacioTabulador+3);
    		
    		interno = 0;
	    	for (int i = 0; i < clase.getAtributos().size(); i++)
	    	{
	    		nombreAtributo = clase.getAtributos().get(i).getNombre();
	    		
	    		if (nombreAtributo.toLowerCase().equals("activo") || 
		    		toCamelCase(nombreAtributo).toLowerCase().equals("fecharegistro") ||
		    		clase.getAtributos().get(i).isPrimaria())	    		
	    		{
	    			continue;
	    		}
	    		else
	    		{
	    			String temp = "    ";
	    			
	    			if (interno == 0)
	    			{
	    				temp = "SET " + nombreAtributo;
	    				interno++;
	    			}
	    			else
	    			{
	    				temp += nombreAtributo;
	    			}
	    			
    				tam = getTamEspacios(nombreAtributo, clase, false);
    				
    				for (int j = 0; j < tam; j++)
    				{
    					temp += " ";
    				}
    				
    				temp += " = '$" + nombreAtributo + "',";
    				
    				imprimir(temp, writer, espacioTabulador+6);
	    		}
	    	}
	    	
			cad = "    ";
			
			if (interno == 0)
			{
				cad = "SET activo";
				interno++;
			}
			else
			{
				cad += "activo";
			}
			
			tam = getTamEspacios("activo", clase, false);
			
			for (int j = 0; j < tam; j++)
			{
				cad += " ";
			}
			
			cad += " = 'S'";
			
			imprimir(cad, writer, espacioTabulador+6);

	    	
    		imprimir("WHERE $table" + nombreClase + "." + clase.getPrimaria().getNombre() + " = '$" + clase.getPrimaria().getNombre() + "'\";", writer, espacioTabulador+6);

			//Continuamos
	        imprimir("", writer, espacioTabulador);
			imprimir("if (DatabaseManager::singleAffectedRow($query) === true)", writer, espacioTabulador+3);
			imprimir("{", writer, espacioTabulador+3);
			imprimir("return true;", writer, espacioTabulador+4);
			imprimir("}", writer, espacioTabulador+3);
			imprimir("else", writer, espacioTabulador+3);
			imprimir("{", writer, espacioTabulador+3);
			imprimir("return false;", writer, espacioTabulador+4);
			imprimir("}", writer, espacioTabulador+3);
			imprimir("}", writer, espacioTabulador+2);
			imprimir("else", writer, espacioTabulador+2);
			imprimir("{", writer, espacioTabulador+2);
			imprimir("return false;", writer, espacioTabulador+3);
			imprimir("}", writer, espacioTabulador+2);
			imprimir("}", writer, espacioTabulador+1);
			imprimir("else", writer, espacioTabulador+1);
			imprimir("{", writer, espacioTabulador+1);
			imprimir("return false;", writer, espacioTabulador+2);
			imprimir("}", writer, espacioTabulador+1);
			imprimir("}", writer, espacioTabulador);
			imprimir("", writer, espacioTabulador);
			
			/* Creacion del metodo filter */
		    imprimir("/**", writer, espacioTabulador);
		    imprimir(" * Recupera varios objeto de tipo " + nombreClase, writer, espacioTabulador);
		    imprimir(" */", writer, espacioTabulador);
		    imprimir("static function filter($keysValues = array())", writer, espacioTabulador);
		    imprimir("{", writer, espacioTabulador);
		    imprimir("if (!is_array($keysValues) || empty($keysValues))", writer, espacioTabulador+1);
		    imprimir("{", writer, espacioTabulador+1);
		    imprimir("return null;", writer, espacioTabulador+2);
		    imprimir("}", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador);
		    imprimir("$table" + nombreClase + "  = DatabaseManager::getNameTable('" + tabla + "');", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador);
		    imprimir("$query     = \"SELECT $table" + nombreClase + ".*", writer, espacioTabulador+1);
		    imprimir("              FROM $table" + nombreClase, writer, espacioTabulador+1);
		    imprimir("              WHERE $table" + nombreClase + ".activo = 'S' AND \";", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador);		    
		    imprimir("foreach ($keysValues as $key => $value)", writer, espacioTabulador+1);
		    imprimir("{", writer, espacioTabulador+1);
		    imprimir("$query .= \"$table" + nombreClase + ".$key = '$value' AND \";", writer, espacioTabulador+2);
		    imprimir("}", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador+1);
		    imprimir("$query = substr($query, 0, strlen($query)-4);", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador+1);
		    imprimir("$query = $query . \" ORDER BY \";", writer, espacioTabulador+1);
		    imprimir("", writer, espacioTabulador+1);

		    //Creamos los if else de los ordenamientos
	        primera = true;
	        posicionPrimaria = -1;
	        for (int i = 0; i < clase.getAtributos().size(); i++)
	        {
	        	if (clase.getAtributos().get(i).isDisimil() && !clase.getAtributos().get(i).isPrimaria())
	        	{
	        		nombreAtributo = clase.getAtributos().get(i).getNombre();
	        		
	        		if (primera)
	        		{
	        			imprimir("if ($order == '" + toCamelCase(nombreAtributo) + "')", writer, espacioTabulador+1);
	        			imprimir("{", writer, espacioTabulador+1);
	        			imprimir("$query = $query . \" $table" + nombreClase + "." + nombreAtributo + "\";", writer, espacioTabulador+2);
	        			imprimir("}", writer, espacioTabulador+1);
	        			primera = false;
	        		}
	        		else
	        		{
	        			imprimir("else if ($order == '" + toCamelCase(nombreAtributo) + "')", writer, espacioTabulador+1);
	        			imprimir("{", writer, espacioTabulador+1);
	        			imprimir("$query = $query . \" $table" + nombreClase + "." + nombreAtributo + "\";", writer, espacioTabulador+2);
	        			imprimir("}", writer, espacioTabulador+1);
	        		}
	        	}
	        	else if (clase.getAtributos().get(i).isPrimaria())
	        	{
	        		posicionPrimaria = i;
	        	}
	        }
	        
	        if (posicionPrimaria != -1)
	        {
        		nombreAtributo = clase.getAtributos().get(posicionPrimaria).getNombre();
    			imprimir("else", writer, espacioTabulador+1);
    			imprimir("{", writer, espacioTabulador+1);
    			imprimir("$query = $query . \" $table" + nombreClase + "." + nombreAtributo + " DESC\";", writer, espacioTabulador+2);
    			imprimir("}", writer, espacioTabulador+1);
	        }
	        
			imprimir("", writer, espacioTabulador);
			imprimir("if ($begin >= 0)", writer, espacioTabulador+1);
			imprimir("{", writer, espacioTabulador+1);
			imprimir("$query = $query. \" LIMIT \" . strval($begin * $cantidad) . \", \" . strval($cantidad+1);", writer, espacioTabulador+2);
			imprimir("}", writer, espacioTabulador+1);
			imprimir("", writer, espacioTabulador);
			imprimir("$array" + nombreClase + "s   = DatabaseManager::multiFetchAssoc($query);", writer, espacioTabulador+1);
			imprimir("$" + nombreClase.toLowerCase() + "_simples = array();", writer, espacioTabulador+1);
			imprimir("", writer, espacioTabulador);
			imprimir("if ($array" + nombreClase + "s !== NULL)", writer, espacioTabulador+1);
			imprimir("{", writer, espacioTabulador+1);
			imprimir("$i = 0;", writer, espacioTabulador+2);
			imprimir("foreach ($array" + nombreClase + "s as $" + nombreClase.toLowerCase() + "_simple) ", writer, espacioTabulador+2);
			imprimir("{", writer, espacioTabulador+2);
			imprimir("if ($i == $cantidad && $begin >= 0)", writer, espacioTabulador+3);
			imprimir("{", writer, espacioTabulador+3);
			imprimir("continue;", writer, espacioTabulador+4);
			imprimir("}", writer, espacioTabulador+3);
			imprimir("", writer, espacioTabulador);
			imprimir("$" + nombreClase.toLowerCase() + "A = new " + nombreClase + "();", writer, espacioTabulador+3);
			imprimir("$" + nombreClase.toLowerCase() + "A->fromArray($" + nombreClase.toLowerCase() + "_simple);", writer, espacioTabulador+3);
			imprimir("$" + nombreClase.toLowerCase() + "_simples[] = $" + nombreClase.toLowerCase() + "A;", writer, espacioTabulador+3);
			imprimir("$i++;", writer, espacioTabulador+3);
			imprimir("}", writer, espacioTabulador+2);
	    	imprimir("", writer, espacioTabulador);
	    	imprimir("return $" + nombreClase.toLowerCase() + "_simples;", writer, espacioTabulador+2);
	    	imprimir("}", writer, espacioTabulador+1);
	    	imprimir("else", writer, espacioTabulador+1);
	    	imprimir("{", writer, espacioTabulador+1);
	    	imprimir("return NULL;", writer, espacioTabulador+2);
	    	imprimir("}", writer, espacioTabulador+1);
	    	imprimir("}", writer, espacioTabulador);
	        imprimir("", writer, espacioTabulador);
	        
	        /* Creacion del Metodo remove */
	        imprimir("/**", writer, espacioTabulador);
	        imprimir(" * Elimina logicamente un " + nombreClase + "", writer, espacioTabulador);
	        imprimir(" */", writer, espacioTabulador);
	        imprimir("static function remove($" + nombreClase.toLowerCase() + " = null)", writer, espacioTabulador);
	        imprimir("{", writer, espacioTabulador);
	        imprimir("if ($alumno === null)", writer, espacioTabulador+1);
	        imprimir("{", writer, espacioTabulador+1);
	        imprimir("return false;", writer, espacioTabulador+2);
	        imprimir("}", writer, espacioTabulador+1);
	        imprimir("else", writer, espacioTabulador+1);
	        imprimir("{", writer, espacioTabulador+1);
		    imprimir("$table" + nombreClase + "  = DatabaseManager::getNameTable('" + tabla + "');", writer, espacioTabulador+2);
		    imprimir("$" + clase.getPrimaria().getNombre() + " = $" + nombreClase.toLowerCase() + "->" + toCamelCase("get_" + clase.getPrimaria().getNombre()) + "();", writer, espacioTabulador+2);
	        imprimir("", writer, espacioTabulador+2);
	        imprimir("$query = \"UPDATE $table" + nombreClase, writer, espacioTabulador+2);
	        imprimir("          SET activo = 'N' WHERE " + clase.getPrimaria().getNombre() + " = $" + clase.getPrimaria().getNombre() + "\";", writer, espacioTabulador+2);
	        imprimir("", writer, espacioTabulador);
	        imprimir("return DatabaseManager::singleAffectedRow($query);", writer, espacioTabulador+2);
	        imprimir("}", writer, espacioTabulador+1);
	        imprimir("}", writer, espacioTabulador);
	        
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

	public static void exportarConfiguracionPHP(LinkedList<Clase> clases)
	{
		String path = "code/ConfigDatabase.php";
		PrintWriter writer;
		int maxEspacio = -1;
		LinkedList<String> nombreTablas = new LinkedList<String>();
		
		for (int i = 0; i < clases.size(); i++)
		{
			String tabla = clases.get(i).getNombre();
			
			tabla = "TABLE_" + tabla.toUpperCase();
			
			if (tabla.length() > maxEspacio)
			{
				maxEspacio = tabla.length();
			}
			
			nombreTablas.add(tabla);
		}
		
		try 
		{
			writer = new PrintWriter(path, "UTF-8");

			//Creamos la parte de arriba de la clase
			writer.println("<?php ");
			writer.println();
			
		    String cad = "define('HOST',";
		    for (int i = 0; i < maxEspacio - 4; i++)
		    {
		    	cad += " ";
		    }
		    
		    imprimir(cad +  " 'localhost');", writer, 1);

		    cad = "define('USER',";
		    for (int i = 0; i < maxEspacio - 4; i++)
		    {
		    	cad += " ";
		    }
		    
		    imprimir(cad +  " '');", writer, 1);

		    cad = "define('PASSWORD',";
		    for (int i = 0; i < maxEspacio - 8; i++)
		    {
		    	cad += " ";
		    }
		    
		    imprimir(cad +  " '');", writer, 1);

		    cad = "define('DATABASE',";
		    for (int i = 0; i < maxEspacio - 8; i++)
		    {
		    	cad += " ";
		    }
		    
		    imprimir(cad +  " '');", writer, 1);
			writer.println();

		    for (int i = 0; i < clases.size(); i++)
		    {
			    cad = "define('" + nombreTablas.get(i) + "', ";
			    for (int j = 0; j < (maxEspacio - nombreTablas.get(i).length()); j++)
			    {
			    	cad += " ";
			    }
			    
			    imprimir(cad +  "'" + clases.get(i).getNombre() + "');", writer, 1);
		    }
			
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
}
