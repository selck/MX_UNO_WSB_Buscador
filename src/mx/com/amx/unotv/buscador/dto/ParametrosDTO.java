package mx.com.amx.unotv.buscador.dto;

import java.io.Serializable;

public class ParametrosDTO implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	private String dominio;
	private String ammbiente;
	private String urlBuscador;
	
	/**
	 * @return the dominio
	 */
	public String getDominio() {
		return dominio;
	}
	/**
	 * @param dominio the dominio to set
	 */
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	/**
	 * @return the ammbiente
	 */
	public String getAmmbiente() {
		return ammbiente;
	}
	/**
	 * @param ammbiente the ammbiente to set
	 */
	public void setAmmbiente(String ammbiente) {
		this.ammbiente = ammbiente;
	}
	/**
	 * @return the urlBuscador
	 */
	public String getUrlBuscador() {
		return urlBuscador;
	}
	/**
	 * @param urlBuscador the urlBuscador to set
	 */
	public void setUrlBuscador(String urlBuscador) {
		this.urlBuscador = urlBuscador;
	}
	
	
}
