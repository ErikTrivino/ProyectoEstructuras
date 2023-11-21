package uniquindio.persistence;



import uniquindio.listasEnlazadas.Cola;
import uniquindio.listasEnlazadas.listas.ListaDoble;
import uniquindio.listasEnlazadas.listas.ListaSimple;
import uniquindio.modelo.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static uniquindio.persistence.ArchivoUtil.leerArchivo;


public class Persistencia {

	public static final String RUTA_ARCHIVO_MARKEPLACE_BINARIO_DAT = "C://td//AppGestionDat.dat";


	;
	public static final String RUTA_ARCHIVO_USUARIOS = "C://td//persistencia//archivos//ArchivoUsuarios.txt";
	public static final String RUTA_ARCHIVO_PROCESOS = "C://td//persistencia//archivos//ArchivoProcesos.txt";
	public static final String RUTA_ARCHIVO_ACTIVIDADES = "C://td//persistencia//archivos//ArchivoActividades.txt";
	public static final String RUTA_ARCHIVO_TAREAS = "C://td//persistencia//archivos//ArchivoTareas.txt";











//	----------------------LOADS------------------------
	/**
	 * GENERAR REGISTRO
	 * @param mensajeLog
	 * @param nivel
	 * @param accion
	 */

// ------------------------------------SERIALIZACI�N(BINARIO) y XML
	/**
	 * EXPORTAR VENDEDOR EN ARCHIVO DE TEXTO
	 * @param
	 * @throws IOException
	 */
	public static void guardarUsuaiosTxt(ListaSimple<UsuarioApp> listaUsuarios) throws IOException {
		String contenido = "";

		for (UsuarioApp usuarioApp : listaUsuarios) {
			String aux = "";
			for (Proceso p:usuarioApp.getListaProcesos()
				 ) {
				aux+=p.getId()+",";
			}
			contenido += usuarioApp.getNombreUsuario()+">@@<"+ usuarioApp.getContrasenia()+">@@<"+ usuarioApp.getTipoUsuario()+">@@<"+ aux+">@@<" +"\n";
		}


		ArchivoUtil.guardarArchivo(RUTA_ARCHIVO_USUARIOS, contenido, false);

	}
	public static void guardarProcesosTxt(ListaDoble<Proceso> listaProcesos) throws IOException {
		String contenido = "";

		for (Proceso proceso : listaProcesos) {
			String aux = "";
			for (Actividad p:proceso.getActividades()
			) {
				aux+=p.getNombre()+",";
			}
			contenido += proceso.getId()+">@@<"+ proceso.getNombre()+">@@<" + aux+">@@<"+"\n";
		}

		ArchivoUtil.guardarArchivo(RUTA_ARCHIVO_PROCESOS, contenido, false);

	}
	public static void guardarActividadesTxt(ListaDoble<Actividad> listaActividades) throws IOException {
		String contenido = "";

		for (Actividad actividad : listaActividades) {
			String aux = "";

			Cola<Tarea> tareaCola = actividad.obtenerCopiaCola();
			while(!tareaCola.estaVacia()){
				aux+=tareaCola.desencolar().getNombre()+",";
			}



			contenido += actividad.getNombre()+">@@<"+ actividad.getDescripcion()+">@@<"+ actividad.isObligatoria()+">@@<" +aux+">@@<" +"\n";
		}

		ArchivoUtil.guardarArchivo(RUTA_ARCHIVO_ACTIVIDADES, contenido, false);

	}
	public static void guardarTareasTxt(ListaDoble<Tarea> listaTareas) throws IOException {
		String contenido = "";

		for (Tarea tarea : listaTareas) {
			contenido += tarea.getNombre()+">@@<"+ tarea.getDescripcion()+">@@<"+tarea.isObligatoria()+">@@<" +tarea.getDuracionMinutos()+">@@<" +"\n";
		}

		ArchivoUtil.guardarArchivo(RUTA_ARCHIVO_TAREAS, contenido, false);

	}
	/**
	 * EXPORTAR PRODUCTO EN ARCHIVO DE TEXTO
	 * @param
	 * @throws IOException
	 */
	static public ArrayList<Tarea> cargarTareas() throws IOException {
		ArrayList<Tarea> tareas = new ArrayList<>();
		ArrayList<String> contenido = leerArchivo(RUTA_ARCHIVO_TAREAS);

		String linea = "";


		for(int i =0; i< contenido.size();i++){
			linea= contenido.get(i);

			Tarea tarea = new Tarea();
			tarea.setNombre(linea.split(">@@<")[0]);
			tarea.setDescripcion(linea.split(">@@<")[1]);
			tarea.setObligatoria(Boolean.parseBoolean(linea.split(">@@<")[2]));
			tarea.setDuracionMinutos(Integer.parseInt(linea.split(">@@<")[3]));
			tareas.add(tarea);
		}

		return tareas;
	}
	public static ArrayList<Proceso> cargarProcesos() throws IOException {
		ArrayList<Proceso> procesos = new ArrayList<>();
		ArrayList<String> contenido = leerArchivo(RUTA_ARCHIVO_PROCESOS);

		String linea = "";


		for(int i =0; i< contenido.size();i++){
			linea= contenido.get(i);

			Proceso proceso = new Proceso();
			proceso.setId(linea.split(">@@<")[0]);
			proceso.setNombre(linea.split(">@@<")[1]);


			String[] actividadess = linea.split(">@@<")[2].split(",");

			ArrayList<Actividad> actividads = cargarActividades();
			for (String p: actividadess
			) {
				for (Actividad actividad :cargarActividades()
				) {
					if(p.equals(actividad.getNombre())){
						proceso.getActividades().agregarfinal(actividad);
					}
				}
			}
			procesos.add(proceso);
		}

		return procesos;
	}
	static public ArrayList<Actividad> cargarActividades() throws IOException {
		ArrayList<Actividad> actividads = new ArrayList<>();
		ArrayList<String> contenido = leerArchivo(RUTA_ARCHIVO_ACTIVIDADES);

		String linea = "";


		for(int i =0; i< contenido.size();i++){
			linea= contenido.get(i);

			Actividad actividad = new Actividad();
			actividad.setNombre(linea.split(">@@<")[0]);
			actividad.setDescripcion(linea.split(">@@<")[1]);
			actividad.setObligatoria(Boolean.parseBoolean(linea.split(">@@<")[2]));
			String[] tareas = linea.split(">@@<")[3].split(",");

			ArrayList<Tarea> tareaArrayList = cargarTareas();
			for (String t:tareas
				 ) {
				for (Tarea tarea:tareaArrayList
					 ) {
					if(t.equals(tarea.getNombre())){
						actividad.getTareas().encolar(tarea);
					}
				}
			}
			actividads.add(actividad);
		}

		return actividads;
	}


	public static ArrayList<UsuarioApp> cargarUsuarios() throws IOException {
		ArrayList<UsuarioApp> listaVendedores = new ArrayList<>();
		ArrayList<String> contenido = leerArchivo(RUTA_ARCHIVO_USUARIOS);
		String linea="";

		for (int i = 0; i < contenido.size(); i++)
		{
			linea = contenido.get(i);
			UsuarioApp usuarioApp = new UsuarioApp();

			usuarioApp.setNombreUsuario(linea.split(">@@<")[0]);
			usuarioApp.setContrasenia(linea.split(">@@<")[1]);
			usuarioApp.setTipoUsuario(TipoUsuario.USUARIOREGULAR);

			//usuarioApp.setListaProcesos();

			String[] procesosId = linea.split(">@@<")[2].split(";");

			ArrayList<Proceso> procesoArrayList = cargarProcesos();
			for (String procesoId:procesosId
				 ) {
				for (Proceso p:procesoArrayList
					  ) {
					if(p.getId().equals(procesoId)){
						usuarioApp.getListaProcesos().agregarfinal(p);
					}
				}
			}

			listaVendedores.add(usuarioApp);

		}


		return listaVendedores;
	}







	/**
	 * CARGAR RECURSO BINARIO
	 * @return
	 */
	public static AppGestion cargarRecursomarkBinario() {
		AppGestion mark = null;
		try {
			mark = (AppGestion) ArchivoUtil.cargarRecursoSerializado(RUTA_ARCHIVO_MARKEPLACE_BINARIO_DAT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mark;
	}
	/**
	 * GUARDAR RECURSO BINARIO
	 * @param mark
	 */
	public static  void guardarRecursoMarkBinario(AppGestion mark) {
		try {
			ArchivoUtil.salvarRecursoSerializado(RUTA_ARCHIVO_MARKEPLACE_BINARIO_DAT, mark);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	/**
	 * CARGAR RECURSO BINARIO
	 * @return
	 */
//	public static A cargarRecursoMarketplaceXML() {
//		Object object = null;
//		MarketPlace mark = null;
//
//
//		try {
//			object = ArchivoUtil.cargarRecursoSerializadoXML(RUTA_ARCHIVO_MARKETPLACE_XML);
//			mark = (MarketPlace) object;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return mark;
//	}
//
//	/**
//	 * GUARDAR RECURSO BINARIO EN XML
//	 * @param mark
//	 */
//	public static  void guardarRecursoMarketplaceXML(MarketPlace mark) {
//		try {
//
//			ArchivoUtil.salvarRecursoSerializadoXML(RUTA_ARCHIVO_MARKETPLACE_XML, mark);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
}
