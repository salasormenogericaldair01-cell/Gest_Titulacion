package pe.edu.suiza.modelo;

/**
 * Modelo Catálogo (carreras, modalidades, asesores) según requerimiento de la documentación.
 * Paquete modular: pe.edu.suiza.modelo
 */
public class Catalogo {
    
    private int idCatalogo;
    private String tipoCatalogo;
    private String codigoItem;
    private String nombreItem;
    private String descripcion;
    private String estado;

    public Catalogo() {
    }

    public Catalogo(int idCatalogo, String tipoCatalogo, String codigoItem, String nombreItem, String descripcion, String estado) {
        this.idCatalogo = idCatalogo;
        this.tipoCatalogo = tipoCatalogo;
        this.codigoItem = codigoItem;
        this.nombreItem = nombreItem;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public int getIdCatalogo() { return idCatalogo; }
    public void setIdCatalogo(int idCatalogo) { this.idCatalogo = idCatalogo; }

    public String getTipoCatalogo() { return tipoCatalogo; }
    public void setTipoCatalogo(String tipoCatalogo) { this.tipoCatalogo = tipoCatalogo; }

    public String getCodigoItem() { return codigoItem; }
    public void setCodigoItem(String codigoItem) { this.codigoItem = codigoItem; }

    public String getNombreItem() { return nombreItem; }
    public void setNombreItem(String nombreItem) { this.nombreItem = nombreItem; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    @Override
    public String toString() {
        return nombreItem;
    }
}
