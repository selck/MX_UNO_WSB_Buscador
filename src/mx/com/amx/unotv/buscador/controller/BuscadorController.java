package mx.com.amx.unotv.buscador.controller;

import java.util.List;

import mx.com.amx.unotv.buscador.bo.BuscadorBO;
import mx.com.amx.unotv.buscador.dto.ContentDTO;
import mx.com.amx.unotv.buscador.dto.ParametrosDTO;
import mx.com.amx.unotv.buscador.dto.WrapperContentDTO;
import mx.com.amx.unotv.buscador.util.CargaParametros;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("buscadorController")
public class BuscadorController {
	
	private static Logger logger=Logger.getLogger(BuscadorController.class);
	
	private BuscadorBO buscadorBO;
		
	@RequestMapping(value={"getNotasRelacionadasWrapper"}, method={org.springframework.web.bind.annotation.RequestMethod.POST}, headers={"Accept=application/json"})
	@ResponseBody
	public WrapperContentDTO getNotasRelacionadasWrapper (@RequestBody String tags){
		
		WrapperContentDTO wrapperContentDTO=new WrapperContentDTO();
		CargaParametros cargaParametros=new CargaParametros();
		ParametrosDTO parametrosDTO=cargaParametros.obtenerPropiedades("ambiente.resources.properties");
		
		logger.info("getNotasRelacionadas -- Controller");
		logger.info("Tags recibidos: "+tags);
		
		try {
			wrapperContentDTO.setListNotas(buscadorBO.getNotasRelacionadas(tags, parametrosDTO));
			
		} catch (Exception e) {
			
			logger.error(" Error getNotasRelacionadasWrapper [Controller] ",e );
		}
		return wrapperContentDTO;
	}
	
	@RequestMapping(value={"getNotasRelacionadas"}, method={org.springframework.web.bind.annotation.RequestMethod.POST}, headers={"Accept=application/json"})
	@ResponseBody
	public List<ContentDTO> getNotasRelacionadas (@RequestBody String tags){
		
		List<ContentDTO> list=null;
		CargaParametros cargaParametros=new CargaParametros();
		ParametrosDTO parametrosDTO=cargaParametros.obtenerPropiedades("ambiente.resources.properties");
		logger.info("getNotasRelacionadas -- Controller");
		logger.info("Tagas recibidos: "+tags);
		try {
			list=buscadorBO.getNotasRelacionadas(tags, parametrosDTO);
			
		} catch (Exception e) {
			
			logger.error(" Error getNotasRelacionadas [Controller] ",e );
		}
		return list;
	}
	/**
	 * @return the buscadorBO
	 */
	public BuscadorBO getBuscadorBO() {
		return buscadorBO;
	}

	/**
	 * @param buscadorBO the buscadorBO to set
	 */
	@Autowired
	public void setBuscadorBO(BuscadorBO buscadorBO) {
		this.buscadorBO = buscadorBO;
	}
	
	
}
