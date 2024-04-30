package co.il.liam.model;

import java.io.Serializable;

public class BaseEntity implements Serializable {
    protected String idFs;
    public String getIdFs() {
        return idFs;
    }
    public void setIdFs(String idFs) {
        this.idFs = idFs;
    }
}