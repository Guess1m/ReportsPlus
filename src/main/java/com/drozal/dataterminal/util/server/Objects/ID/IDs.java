package com.drozal.dataterminal.util.server.Objects.ID;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * The type Ds.
 */
@XmlRootElement(name = "IDs")
@XmlAccessorType(XmlAccessType.FIELD)
public class IDs {

    @XmlElement(name = "ID")
    private List<ID> idList;

    /**
     * Gets id list.
     *
     * @return the id list
     */
    public List<ID> getIdList() {
        return idList;
    }

    /**
     * Sets id list.
     *
     * @param idList the id list
     */
    public void setIdList(List<ID> idList) {
        this.idList = idList;
    }
}
