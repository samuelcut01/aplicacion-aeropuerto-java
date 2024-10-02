package modelo;

import java.util.List;

public class Aeropuerto {

	private Integer id;
	private String nombre;
	private String ciudad;
	private List<Vuelo> listaVuelos;

	public Aeropuerto(String nombre, String ciudad) {
		this.nombre = nombre;
		this.ciudad = ciudad;
	}

	public Aeropuerto() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public List<Vuelo> getListaVuelos() {
		return listaVuelos;
	}

	public void setListaVuelos(List<Vuelo> listaVuelos) {
		this.listaVuelos = listaVuelos;
	}

	public String toString() {
		return "Aeropuerto [id=" + id + ", nombre=" + nombre + ", ciudad=" + ciudad + ", listaVuelos=" + "]";
	}

}
