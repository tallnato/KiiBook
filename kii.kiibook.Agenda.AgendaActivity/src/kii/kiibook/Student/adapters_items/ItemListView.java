
package kii.kiibook.Student.adapters_items;

public class ItemListView {
    
    private String texto;
    private int    iconeRid;
    
    public ItemListView( String texto, int iconeRid ) {
    
        this.texto = texto;
        this.iconeRid = iconeRid;
    }
    
    public ItemListView( String texto ) {
    
        this.texto = texto;
        iconeRid = android.R.drawable.ic_menu_agenda;
    }
    
    public String getTexto() {
    
        return texto;
    }
    
    public int getIconeRid() {
    
        return iconeRid;
    }
    
    public void setTexto( String texto ) {
    
        this.texto = texto;
    }
    
    public void setIconeRid( int iconeRid ) {
    
        this.iconeRid = iconeRid;
    }
    
}
