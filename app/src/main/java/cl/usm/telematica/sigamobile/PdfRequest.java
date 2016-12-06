package cl.usm.telematica.sigamobile;

/**
 * Created by Pipos on 05-12-2016.
 */

public class PdfRequest {
    private String plan;
    private String rut;
    private String dv;
    private String mant;
    private String carrera;
    private String mencion;
    private String sedea;
    private String sedec;
    private String plan_alumno;
    private String tipo;
    private String description;

    public PdfRequest(){}

    public PdfRequest(String plan, String rut, String dv, String mant, String carrera,
                      String mencion, String sedea, String sedec, String plan_alumno, String tipo,
                      String description){
        this.plan = plan;
        this.rut = rut;
        this.dv = dv;
        this.mant = mant;
        this.carrera = carrera;
        this.mencion = mencion;
        this.sedea = sedea;
        this.sedec = sedec;
        this.plan_alumno = plan_alumno;
        this.tipo = tipo;
        this.description = description;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getMant() {
        return mant;
    }

    public void setMant(String mant) {
        this.mant = mant;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getMencion() {
        return mencion;
    }

    public void setMencion(String mencion) {
        this.mencion = mencion;
    }

    public String getSedea() {
        return sedea;
    }

    public void setSedea(String sedea) {
        this.sedea = sedea;
    }

    public String getSedec() {
        return sedec;
    }

    public void setSedec(String sedec) {
        this.sedec = sedec;
    }

    public String getPlan_alumno() {
        return plan_alumno;
    }

    public void setPlan_alumno(String plan_alumno) {
        this.plan_alumno = plan_alumno;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
