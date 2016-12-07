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
		    
		    //TODO Agregar Ciclo para validar el archivo
			
			b.close();
		} 
		catch (IOException e) 
		{
			archivo = "";
		}
		
		if (archivo != "")
		{
			return null;
		}
		else
		{
			return null;
		}
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
				
				writer.write(claseTemp.getId());
				writer.write(tokenNodoClase);
				writer.write(claseTemp.getNombre());
				writer.write(tokenNodoClase);
				
				for (int j = 0; j < claseTemp.getAtributos().size(); j++)
				{
					AtributoClase atributo = claseTemp.getAtributos().get(j);
					
					writer.write(atributo.getId());
					writer.write(tokenNodoAtributo);
					
					writer.write(atributo.getNombre());
					writer.write(tokenNodoAtributo);
					
					writer.write(atributo.getTipo());
					writer.write(tokenNodoAtributo);
					
					writer.write(atributo.getDescripcion());
					writer.write(tokenNodoAtributo);
					
					writer.write(atributo.getDefecto());
					writer.write(tokenNodoAtributo);
					
					if (atributo.isPrimaria())
					{
						writer.write("1");
					}
					else
					{
						writer.write("0");
					}
					writer.write(tokenNodoAtributo);

					if (atributo.isForanea())
					{
						writer.write("1");
					}
					else
					{
						writer.write("0");
					}
					writer.write(tokenNodoAtributo);

					if (atributo.isNula())
					{
						writer.write("1");
					}
					else
					{
						writer.write("0");
					}
					writer.write(tokenNodoAtributo);

					if (atributo.isDisimil())
					{
						writer.write("1");
					}
					else
					{
						writer.write("0");
					}
					writer.write(tokenNodoAtributo);
					writer.write(tokenAtributo);
				}
				
				writer.write(tokenNodoClase);
				
				if (claseTemp.isRelacion())
				{
					writer.write("1");
				}
				else
				{
					writer.write("0");
				}
				
				writer.write(tokenNodoClase);
				writer.write(tokenClase);
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
