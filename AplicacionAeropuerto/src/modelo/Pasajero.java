package modelo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pasajero {

	private Integer id;
	private String nombre;
	private String apellidos;
	private String dNI;

	public Pasajero(String nombre, String apellidos, String dNI) throws IllegalArgumentException {
		this.nombre = nombre;
		this.apellidos = apellidos;
		try {
			this.dNI = validarDNI(dNI);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error al crear el pasajero: " + e.getMessage());
		}
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getdNI() {
		return dNI;
	}

	public void setdNI(String dNI) {
		this.dNI = dNI;
	}

	private static String validarDNI(String dNI) throws IllegalArgumentException {
		Pattern dNIPattern = Pattern.compile("^(\\d{8})([A-Z])$");
		Matcher matcher = dNIPattern.matcher(dNI);
		if (matcher.matches()) {
			int numeroDNI = Integer.parseInt(matcher.group(1));
			char letraDNI = matcher.group(2).charAt(0);
			char letraCorrecta = calcularLetraDNI(numeroDNI);
			if (letraDNI == letraCorrecta) {
				return dNI;
			} else {
				throw new IllegalArgumentException("La letra del DNI proporcionado no es correcta.");
			}
		} else {
			throw new IllegalArgumentException("El formato del DNI proporcionado no es válido.");
		}
	}

	private static char calcularLetraDNI(int numeroDNI) {
		String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
		return letras.charAt(numeroDNI % 23);
	}

	public String toString() {
		return "Pasajero [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", dNI=" + dNI + "]";
	}
}
