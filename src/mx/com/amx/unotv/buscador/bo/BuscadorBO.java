package mx.com.amx.unotv.buscador.bo;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import mx.com.amx.unotv.buscador.dto.ContentDTO;
import mx.com.amx.unotv.buscador.dto.ParametrosDTO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
@Qualifier("buscadorBO")
public class BuscadorBO {

private static Logger logger=Logger.getLogger(BuscadorBO.class);

	public List<ContentDTO> getNotasRelacionadas(String tags, ParametrosDTO parametrosDTO) {
		
		//String urlBuscador="http://AMXSVROUT01-1.tmx-internacional.net/s/search?q=epn&site=unotv&access=p&client=unotv&output=xml_no_dtd&proxyreload=1&getfields=*&num=50";
		String urlBuscador=parametrosDTO.getUrlBuscador();
		
		logger.info("Inicia getListRelacionadas ");
		logger.info("tags: "+tags);
		//logger.info("urlBuscador: "+urlBuscador);		
		
		List<ContentDTO> list = new ArrayList<ContentDTO>();
		String urlServidor = "";
		String base= parametrosDTO.getAmmbiente().equalsIgnoreCase("desarrollo")?"Keywords:$PALABRA$|":"nota_tags:$PALABRA$|";
		//String base = "Keywords:$PALABRA$|";
		//String base = "nota_tags:$PALABRA$|";
		ContentDTO contentDTO = null;
		try {
			
			String palabras[] = tags.split("\\,");
			String query = "";
			
			for (String palabra : palabras) {
				palabra = replaceGSA(palabra.trim());
				query += base.replace("$PALABRA$", palabra);
			}
			urlServidor = urlBuscador + query;
			urlServidor = urlServidor.substring(0, urlServidor.length() - 1);
			logger.info("URL GSA: " + urlServidor);
			URL url = new URL(urlServidor);
			URLConnection urlCon = url.openConnection();
			if(parametrosDTO.getAmmbiente().equalsIgnoreCase("produccion"))
				urlCon.setRequestProperty("X-Target", "buscador.unotv.com");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(urlCon.getInputStream());
			doc.getDocumentElement().normalize();

			logger.info("Root element :" +doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("R");
			
			
			logger.info("Recorremos las notas de la busqueda");
			logger.info("Total de notas: "+nList.getLength());
			//Recorremos las notas
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
				
				contentDTO = new ContentDTO();
				
				Node nNode = nList.item(temp);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
										
					//logger.info("T (Titulo GSA)  : "+eElement.getElementsByTagName("T").item(0).getTextContent());
					logger.info("U (URL Nota)    : "+eElement.getElementsByTagName("U").item(0).getTextContent());					
					NodeList nodeListMT =   eElement.getElementsByTagName("MT");
					contentDTO.setFcUrl(eElement.getElementsByTagName("U").item(0).getTextContent());
					//Obtenemos los metas de la nota:
					//logger.info("== Metas de la nota: "+nodeListMT.getLength());
					//logger.info("Num metas: "+nodeListMT.getLength());
					for (int i = 0; i < nodeListMT.getLength(); i++) {					
						Node nNodeMeta = nodeListMT.item(i);																
						 NamedNodeMap namedNodeMap = nNodeMeta.getAttributes();						 						
						 contentDTO = obtieneMeta(contentDTO, namedNodeMap.getNamedItem("N").getTextContent().trim(), namedNodeMap.getNamedItem("V").getTextContent().trim());
					}

					list.add(contentDTO);
				
				}
			}
		} catch (Exception e) {
			logger.error("Error getListRelacionadas: ", e);
			return Collections.emptyList();
		}
		return list;
	}
	
	private static String replaceGSA(String cadena) {
		
		try {
			
			cadena = cadena.replaceAll(" ", "%2520");

			cadena = cadena.replaceAll("á", "%C3%A1");
			cadena = cadena.replaceAll("é", "%C3%A9");
			cadena = cadena.replaceAll("í", "%C3%AD");
			cadena = cadena.replaceAll("ó", "%C3%B3");
			cadena = cadena.replaceAll("ú", "%C3%BA");

			cadena = cadena.replaceAll("Á", "%C3%81");
			cadena = cadena.replaceAll("É", "%C3%89");
			cadena = cadena.replaceAll("Í", "%C3%8D");
			cadena = cadena.replaceAll("Ó", "%C3%93");
			cadena = cadena.replaceAll("Ú", "%C3%9A");

		} catch (Exception e) {
			logger.error("Error replace GSA: " + e.getMessage());
			return "";
		}
		return cadena;
	}
	
	/**
	 * Metodo que coloca los tag
	 * */
	private ContentDTO obtieneMeta(ContentDTO contentDTO, String N, String V)
	{
		//logger.debug("Inicia obtieneMeta");
		
		try {
			
			if(V == null)
			{
				V="";
			}
			
			//
			if(N.equals("nota_published_time"))
			{
				//contentDTO.setFdFechaPublicacion(V.trim());
			}
			
						
			if(N.equals("nota_modified_time"))
			{
				//contentDTO.setFcIdTipoNota(V.trim());
			}
			
			
			if(N.equals("nota_tipo"))
			{
				contentDTO.setFcIdTipoNota(V.trim());
			}
			
			//if(N.equals("nota_img"))
			//Usamos las de twitter
			if(N.equals("twitter:image"))
			{
				contentDTO.setFcImgPrincipal(V.trim().replaceAll("http://www.unotv.com", ""));
			}
			
			if(N.equals("nota_tipo_seccion"))
			{
				contentDTO.setFcTipoSeccion(V.trim());
			}
			
			if(N.equals("nota_seccion"))
			{
				contentDTO.setFcSeccion(V.trim());
			}
			
			
			if(N.equals("nota_categoria"))
			{
				/*String url="http://www.unotv.com/noticias/portal/turismo/detalle/5likesmiradores-405553/";
				url=url.substring(0,url.indexOf("/detalle"));
				String [] list=url.split("\\/");
				System.out.println(list[list.length - 1]);*/
				contentDTO.setFcIdCategoria(V.trim());
			}
			
			
			if(N.equals("nota_tags"))
			{
				contentDTO.setFcTags(V.trim());
			}
									
			//if(N.equals("nota_titulo"))
			//Usamos las de twitter
			if(N.equals("twitter:title"))
			{
				contentDTO.setFcTitulo(V.trim());
			}
			
			
			
		} catch (Exception e) {
			logger.error("Exception en obtieneMeta");
		}
		
		return contentDTO;		
	}


	
}
