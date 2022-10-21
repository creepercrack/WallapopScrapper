public class Producto {
    private String url;
    private String comprador;
    private String vendedor;
    private String titulo;
    private String precio;
    private String estado;
    private String tipo;
    private String descripcion;
    private String fecha;

    public Producto(String url, String comprador, String vendedor) {
        this.url = url;
        this.comprador = comprador;
        this.vendedor = vendedor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Producto{" +
            "url='" + url + '\'' +
            ", comprador='" + comprador + '\'' +
            ", vendedor='" + vendedor + '\'' +
            ", titulo='" + titulo + '\'' +
            ", precio='" + precio + '\'' +
            ", estado='" + estado + '\'' +
            ", tipo='" + tipo + '\'' +
            ", descripcion='" + descripcion + '\'' +
            ", fecha='" + fecha + '\'' +
            '}';
    }
}
