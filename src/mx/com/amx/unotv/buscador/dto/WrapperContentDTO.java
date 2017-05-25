package mx.com.amx.unotv.buscador.dto;

import java.util.List;

public class WrapperContentDTO {
	
	List<ContentDTO> listNotas;

	/**
	 * @return the listNotas
	 */
	public List<ContentDTO> getListNotas() {
		return listNotas;
	}

	/**
	 * @param listNotas the listNotas to set
	 */
	public void setListNotas(List<ContentDTO> listNotas) {
		this.listNotas = listNotas;
	}
	
}
