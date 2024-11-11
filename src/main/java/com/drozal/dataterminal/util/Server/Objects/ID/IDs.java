package com.drozal.dataterminal.util.Server.Objects.ID;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "IDs")
@XmlAccessorType(XmlAccessType.FIELD)
public class IDs {
	
	@XmlElement(name = "ID")
	private List<ID> idList;
	
	public List<ID> getIdList() {
		return idList;
	}
	
	public void setIdList(List<ID> idList) {
		this.idList = idList;
	}
}
