import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class ManejadorArchivos 
{
	public static LinkedList<Clase> importar(String nombreArchivo)
	{
		String archivo = "";
		LinkedList<Clase> clases = new LinkedList<Clase>();		
		char tokenNodoClase = '*';
		char tokenClase = '$';
		char tokenAtributo = '&';
		char tokenNodoAtributo = '|';

		//Obtenemos el contenido del archivo
		try  
		{
			String cadena = "";
			FileReader f = new FileReader(nombreArchivo);
			BufferedReader b = new BufferedReader(f);
			
		    while((cadena = b.readLine())!=null) 
		    {		    	
		    	archivo += cadena;
		    }
		    
		    String cadenaAcumulativa = "";
		    AtributoClase attr = new AtributoClase();
		    Clase clase = new Clase();
		    int contadorAtributo = 0;
		    int contadorClase = 0;
		    
		    for (int i = 0; i < archivo.length(); i++)
		    {
		    	if (archivo.charAt(i) == tokenNodoClase)
		    	{
		    		if (contadorClase == 0)
		    		{
		    			int num = 0;
		    			
		    			try
		    			{
		    				num = Integer.parseInt(cadenaAcumulativa);
		    			}
		    			catch (NumberFormatException e)
		    			{
		    				e.printStackTrace();
		    			}
		    			
		    			cadenaAcumulativa = "";
		    			contadorClase++;
		    			clase.setId(num);
		    		}
		    		else if (contadorClase == 1)
		    		{
		    			clase.setNombre(cadenaAcumulativa);
		    			cadenaAcumulativa = "";
		    			contadorClase++;
		    		}
		    		else if (contadorClase == 2)
		    		{
		    			cadenaAcumulativa = "";
		    			contadorClase++;
		    		}
		    		else if (contadorClase == 3)
		    		{
		    			int num = 0;
		    			
		    			try
		    			{
		    				num = Integer.parseInt(cadenaAcumulativa);
		    			}
		    			catch (NumberFormatException e)
		    			{
		    				e.printStackTrace();
		    			}
		    			
		    			if (num == 1)
		    			{
		    				clase.setRelacion(true);
		    			}
		    			else
		    			{
		    				clase.setRelacion(false);
		    			}
		    			
		    			cadenaAcumulativa = "";
		    			contadorClase++;
		    		}
		    	}
		    	else if (archivo.charAt(i) == tokenClase)
		    	{
		    		clases.add(clase);
	    			cadenaAcumulativa = "";
	    			contadorClase = 0;
	    			contadorAtributo = 0;
		    		clase = new Clase();
		    	}
		    	else if (archivo.charAt(i) == tokenNodoAtributo)
		    	{
		    		if (contadorAtributo == 0)
		    		{
		    			int num = 0;
		    			
		    			try
		    			{
		    				num = Integer.parseInt(cadenaAcumulativa);
		    			}
		    			catch (NumberFormatException e)
		    			{
		    				e.printStackTrace();
		    			}

		    			attr.setId(num);
		    			cadenaAcumulativa = "";
		    			contadorAtributo++;
		    		}
		    		else if (contadorAtributo == 1)
		    		{
		    			attr.setNombre(cadenaAcumulativa);
		    			cadenaAcumulativa = "";
		    			contadorAtributo++;
		    		}
		    		else if (contadorAtributo == 2)
		    		{
		    			int num = 0;
		    			
		    			try
		    			{
		    				num = Integer.parseInt(cadenaAcumulativa);
		    			}
		    			catch (NumberFormatException e)
		    			{
		    				e.printStackTrace();
		    			}

		    			attr.setTipo(num);
		    			cadenaAcumulativa = "";
		    			contadorAtributo++;
		    		}
		    		else if (contadorAtributo == 3)
		    		{
		    			attr.setDescripcion(cadenaAcumulativa);
		    			cadenaAcumulativa = "";
		    			contadorAtributo++;
		    		}
		    		else if (contadorAtributo == 4)
		    		{
		    			attr.setDefecto(cadenaAcumulativa);
		    			cadenaAcumulativa = "";
		    			contadorAtributo++;
		    		}
		    		else if (contadorAtributo == 5)
		    		{
		    			int num = 0;
		    			
		    			try
		    			{
		    				num = Integer.parseInt(cadenaAcumulativa);
		    			}
		    			catch (NumberFormatException e)
		    			{
		    				e.printStackTrace();
		    			}
		    			
		    			if (num == 1)
		    			{
		    				attr.setPrimaria(true);
		    			}
		    			else
		    			{
		    				attr.setPrimaria(false);
		    			}
		    			
		    			cadenaAcumulativa = "";
		    			contadorAtributo++;
		    		}
		    		else if (contadorAtributo == 6)
		    		{
		    			int num = 0;
		    			
		    			try
		    			{
		    				num = Integer.parseInt(cadenaAcumulativa);
		    			}
		    			catch (NumberFormatException e)
		    			{
		    				e.printStackTrace();
		    			}
		    			
		    			if (num == 1)
		    			{
		    				attr.setForanea(true);
		    			}
		    			else
		    			{
		    				attr.setForanea(false);
		    			}
		    			
		    			cadenaAcumulativa = "";
		    			contadorAtributo++;

		    		}
		    		else if (contadorAtributo == 7)
		    		{
		    			int num = 0;
		    			
		    			try
		    			{
		    				num = Integer.parseInt(cadenaAcumulativa);
		    			}
		    			catch (NumberFormatException e)
		    			{
		    				e.printStackTrace();
		    			}
		    			
		    			if (num == 1)
		    			{
		    				attr.setNula(true);
		    			}
		    			else
		    			{
		    				attr.setNula(false);
		    			}
		    			
		    			cadenaAcumulativa = "";
		    			contadorAtributo++;
		    		}
		    		else 
		    		{
		    			int num = 0;
		    			
		    			try
		    			{
		    				num = Integer.parseInt(cadenaAcumulativa);
		    			}
		    			catch (NumberFormatException e)
		    			{
		    				e.printStackTrace();
		    			}
		    			
		    			if (num == 1)
		    			{
		    				attr.setDisimil(true);
		    			}
		    			else
		    			{
		    				attr.setDisimil(false);
		    			}
		    			
		    			cadenaAcumulativa = "";
		    			contadorAtributo++;
		    		}
		    	}
		    	else if (archivo.charAt(i) == tokenAtributo)
		    	{
		    		clase.getAtributos().add(attr);
		    		attr = new AtributoClase();
		    		contadorAtributo = 0;
		    		cadenaAcumulativa = "";
		    	}
		    	else
		    	{
		    		cadenaAcumulativa += archivo.charAt(i);
		    	}
		    }
			
			b.close();
		} 
		catch (IOException e) 
		{
			archivo = "";
		}
		
		return clases;
	}
	
	public static void exportar(String nombreArchivo, LinkedList<Clase> listaClases)
	{
		PrintWriter writer;
		
		try 
		{
			writer = new PrintWriter(nombreArchivo, "UTF-8");
			String tokenNodoClase = "*";
			String tokenClase = "$";
			String tokenAtributo = "&";
			String tokenNodoAtributo = "|";
			
			for (int i = 0; i < listaClases.size(); i++)
			{
				Clase claseTemp = listaClases.get(i);
				
				writer.print(claseTemp.getId());
				writer.print(tokenNodoClase);
				writer.print(claseTemp.getNombre());
				writer.print(tokenNodoClase);
				
				for (int j = 0; j < claseTemp.getAtributos().size(); j++)
				{
					AtributoClase atributo = claseTemp.getAtributos().get(j);
					
					writer.print(atributo.getId());
					writer.print(tokenNodoAtributo);
					
					writer.print(atributo.getNombre());
					writer.print(tokenNodoAtributo);
					
					writer.print(atributo.getTipo());
					writer.print(tokenNodoAtributo);
					
					writer.print(atributo.getDescripcion());
					writer.print(tokenNodoAtributo);
					
					writer.print(atributo.getDefecto());
					writer.print(tokenNodoAtributo);
					
					if (atributo.isPrimaria())
					{
						writer.print("1");
					}
					else
					{
						writer.print("0");
					}
					writer.print(tokenNodoAtributo);

					if (atributo.isForanea())
					{
						writer.print("1");
					}
					else
					{
						writer.print("0");
					}
					writer.print(tokenNodoAtributo);

					if (atributo.isNula())
					{
						writer.print("1");
					}
					else
					{
						writer.print("0");
					}
					writer.print(tokenNodoAtributo);

					if (atributo.isDisimil())
					{
						writer.print("1");
					}
					else
					{
						writer.print("0");
					}
					writer.print(tokenNodoAtributo);
					writer.print(tokenAtributo);
				}
				
				writer.print(tokenNodoClase);
				
				if (claseTemp.isRelacion())
				{
					writer.print("1");
				}
				else
				{
					writer.print("0");
				}
				
				writer.print(tokenNodoClase);
				writer.print(tokenClase);
			}
			
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
