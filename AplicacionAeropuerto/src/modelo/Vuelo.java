package modelo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Vuelo {

	private static int contadorCodigo = 1000;

	private Integer id;
	private String codigo;
	private String origen;
	private String destino;
	private LocalDateTime fechaSalida;
	private LocalDateTime fechaLlegada;
	private LocalTime duracion;
	private Integer numeroPlazas;
	private Integer numeroPasajeros;
	private Boolean completo;
	private List<Aeropuerto> listaAeropuertos;
	private List<Pasajero> listaPasajeros;

	public Vuelo(String origen, String destino, LocalDateTime fechaSalida, LocalDateTime fechaLlegada,
			Integer numeroPlazas) {

		this.codigo = calcularCodigo(origen, destino);
		this.origen = origen;
		this.destino = destino;
		this.fechaSalida = fechaSalida;
		this.fechaLlegada = fechaLlegada;
		this.duracion = calcularDuracion();
		this.numeroPlazas = numeroPlazas;
		this.numeroPasajeros = calcularNumeroPasajeros();
		this.completo = calcularCompleto();

	}

	public Vuelo() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public LocalDateTime getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(LocalDateTime fechaSalida) {
		this.fechaSalida = fechaSalida;
		this.duracion = calcularDuracion();
	}

	public LocalDateTime getFechaLlegada() {
		return fechaLlegada;
	}

	public void setFechaLlegada(LocalDateTime fechaLlegada) {
		this.fechaLlegada = fechaLlegada;
		this.duracion = calcularDuracion();
	}

	public LocalTime getDuracion() {
		return duracion;
	}

	public void setDuracion(LocalTime duracion) {
		this.duracion = duracion;
	}

	public Integer getNumeroPlazas() {
		return numeroPlazas;
	}

	public void setNumeroPlazas(Integer numeroPlazas) {
		this.numeroPlazas = numeroPlazas;
		this.completo = calcularCompleto();
	}

	public Integer getNumeroPasajeros() {
		return numeroPasajeros;
	}

	public void setNumeroPasajeros(Integer numeroPasajeros) {
		this.numeroPasajeros = numeroPasajeros;
	}

	public Boolean getCompleto() {
		return completo;
	}

	public void setCompleto(Boolean completo) {
		this.completo = completo;
	}

	public List<Aeropuerto> getListaAeropuertos() {
		return listaAeropuertos;
	}

	public void setListaAeropuertos(List<Aeropuerto> listaAeropuertos) {
		this.listaAeropuertos = listaAeropuertos;
	}

	public List<Pasajero> getListaPasajeros() {
		return listaPasajeros;
	}

	public void setListaPasajeros(List<Pasajero> listaPasajeros) {
		this.listaPasajeros = listaPasajeros;
		this.numeroPasajeros = calcularNumeroPasajeros();
		this.completo = calcularCompleto();
	}

	public String toString() {
		return "Vuelo [id=" + id + ", codigo=" + codigo + ", origen=" + origen + ", destino=" + destino
				+ ", fechaSalida=" + fechaSalida + ", fechaLlegada=" + fechaLlegada + ", duracion=" + duracion
				+ ", numeroPlazas=" + numeroPlazas + ", numeroPasajeros=" + numeroPasajeros + ", completo=" + completo
				+ "]";
	}

	private LocalTime calcularDuracion() {
		if (fechaSalida != null && fechaLlegada != null) {
			Duration duration = Duration.between(fechaSalida, fechaLlegada);
			long hours = duration.toHours();
			long minutes = duration.toMinutes() % 60;
			return LocalTime.of((int) hours, (int) minutes);
		}
		return null;
	}

	private Integer calcularNumeroPasajeros() {
		if (this.listaPasajeros != null) {
			return this.listaPasajeros.size();
		} else {
			return 0;
		}
	}

	private boolean calcularCompleto() {
		if (this.numeroPasajeros != null && this.numeroPlazas != null) {
			return this.numeroPasajeros >= this.numeroPlazas;
		} else {
			return false;
		}
	}

	private String calcularCodigo(String origen, String destino) {
		String letrasOrigen = origen.substring(0, 3).toUpperCase();
		String letrasDestino = destino.substring(0, 3).toUpperCase();
		String codigo = String.format("%d%s%s", contadorCodigo++, letrasOrigen, letrasDestino);
		return codigo;
	}

}
