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

	public static void exportarClase(Clase clase)
	{
		String nombreClase = toCapitalCase(clase.getNombre());
		String path = "code/" + nombreClase + ".php";
		PrintWriter writer;
		int espacioTabulador = 0;
		
		try 
		{
			writer = new PrintWriter(path, "UTF-8");

			writer.println("<?php ");
			writer.println();
			espacioTabulador++;
			imprimir("/**", writer, espacioTabulador);
			imprimir("* Clase para el " + nombreClase, writer, espacioTabulador);
			imprimir("*/", writer, espacioTabulador);
			imprimir("Class " + nombreClase, writer, espacioTabulador);
			imprimir("{", writer, espacioTabulador);
			espacioTabulador++;
			
			for (int i = 0; i < clase.getAtributos().size(); i++)
			{
				imprimir("private $" + toCamelCase(clase.getAtributos().get(i).getNombre()) + ";", writer, espacioTabulador);
			}
			
			writer.println("");
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
