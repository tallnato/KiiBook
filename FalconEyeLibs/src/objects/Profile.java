
package objects;

import java.io.Serializable;

public class Profile implements Serializable {
    
    private static final long serialVersionUID = -6548843282960499555L;
    
    private String            name;
    private Data              aniversary;
    private int               bi;
    private int               numTelf;
    private String            email;
    private Sex               sex;
    private Address           addr;
    
    public Profile( String name, Data aniversary, int bi, int numTelf, String email, Sex sex, Address addr ) {
    
        super();
        this.name = name;
        this.aniversary = aniversary;
        this.bi = bi;
        this.numTelf = numTelf;
        this.email = email;
        this.sex = sex;
        this.addr = addr;
    }
    
    public String getName() {
    
        return name;
    }
    
    public void setName( String name ) {
    
        this.name = name;
    }
    
    public Data getAniversary() {
    
        return aniversary;
    }
    
    public void setAniversary( Data aniversary ) {
    
        this.aniversary = aniversary;
    }
    
    public int getBi() {
    
        return bi;
    }
    
    public void setBi( int bi ) {
    
        this.bi = bi;
    }
    
    public int getNumTelf() {
    
        return numTelf;
    }
    
    public void setNumTelf( int numTelf ) {
    
        this.numTelf = numTelf;
    }
    
    public String getEmail() {
    
        return email;
    }
    
    public void setEmail( String email ) {
    
        this.email = email;
    }
    
    public Sex getSex() {
    
        return sex;
    }
    
    public void setSex( Sex sex ) {
    
        this.sex = sex;
    }
    
    public Address getAddr() {
    
        return addr;
    }
    
    public void setAddr( Address addr ) {
    
        this.addr = addr;
    }
    
    @Override
    public String toString() {
    
        return "Profile [name=" + name + ", aniversary=" + aniversary + ", bi=" + bi + ", numTelf=" + numTelf + ", email=" + email
                                        + ", sex=" + sex + ", addr=" + addr + "]";
    }
    
}
