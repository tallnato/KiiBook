
package objects;

public class Address {
    
    private String country;
    private String city;
    private String local;
    private String street;
    private int    door;
    private String floorApart = "";
    
    public Address( String country, String city, String local, String street, int door, String floorApart ) {
    
        super();
        this.country = country;
        this.city = city;
        this.local = local;
        this.street = street;
        this.door = door;
        this.floorApart = floorApart;
    }
    
    public Address( String country, String city, String local, String street, int door ) {
    
        super();
        this.country = country;
        this.city = city;
        this.local = local;
        this.street = street;
        this.door = door;
    }
    
    public String getCountry() {
    
        return country;
    }
    
    public void setCountry( String country ) {
    
        this.country = country;
    }
    
    public String getCity() {
    
        return city;
    }
    
    public void setCity( String city ) {
    
        this.city = city;
    }
    
    public String getLocal() {
    
        return local;
    }
    
    public void setLocal( String local ) {
    
        this.local = local;
    }
    
    public String getStreet() {
    
        return street;
    }
    
    public void setStreet( String street ) {
    
        this.street = street;
    }
    
    public int getDoor() {
    
        return door;
    }
    
    public void setDoor( int door ) {
    
        this.door = door;
    }
    
    public String getFloorApart() {
    
        return floorApart;
    }
    
    public void setFloorApart( String floorApart ) {
    
        this.floorApart = floorApart;
    }
    
    @Override
    public String toString() {
    
        return "Address [country=" + country + ", city=" + city + ", local=" + local + ", street=" + street + ", door="
                                        + door + ", floorApart=" + floorApart + "]";
    }
    
}
