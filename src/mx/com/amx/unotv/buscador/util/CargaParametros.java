package mx.com.amx.unotv.buscador.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import mx.com.amx.unotv.buscador.dto.ParametrosDTO;

public class CargaParametros {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public ParametrosDTO obtenerPropiedades(String properties) {
		ParametrosDTO parametrosDTO = new ParametrosDTO();		 
		try {	    		
			Properties propsTmp = new Properties();
			propsTmp.load(this.getClass().getResourceAsStream( "/general.properties" ));
			String ambiente = propsTmp.getProperty("ambiente");
			String rutaProperties = propsTmp.getProperty(properties.replace("ambiente", ambiente));
			Properties props = new Properties();
			
			props.load(new FileInputStream(new File(rutaProperties)));				
			parametrosDTO.setDominio(props.getProperty("dominio"));
			parametrosDTO.setAmmbiente(props.getProperty("ambiente"));
			parametrosDTO.setUrlBuscador(props.getProperty("urlBuscador"));
			
		} catch (Exception ex) {
			parametrosDTO = new ParametrosDTO();
			logger.error("No se encontro el Archivo de propiedades: ", ex);			
		}
		return parametrosDTO;
    }
	
}
