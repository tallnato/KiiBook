package kii.kiibook.managerclass.objects;

import kii.kiibook.managerclass.utils.Data;


public class Student extends Profile {
    
    private String       ipAdrress  = "";
    private String       nickname   = "";
    private boolean      attendance = false;
    private final int    numStudent = 12;
    private final double average    = 4.5;
    
    public Student( String name, Data aniversary, int bi, int numTelf, String email, Sex sex, Address addr,
                                    String ipAdd, String nickname ) {
    
        super(name, aniversary, bi, numTelf, email, sex, addr);
        ipAdrress = ipAdd;
        this.nickname = nickname;
    }
    
    public Student( String name, Data aniversary, int bi, int numTelf, String email, Sex sex, Address addr ) {
    
        super(name, aniversary, bi, numTelf, email, sex, addr);
    }
    
    public int getNumStudent() {
    
        return numStudent;
    }
    
    public double getAverage() {
    
        return average;
    }
    
    public boolean isAttendance() {
    
        return attendance;
    }
    
    public void setAttendance( boolean attd ) {
    
        attendance = attd;
    }
    
    public String getIpAdrress() {
    
        return ipAdrress;
    }
    
    public void setIpAdrress( String ipAdrress ) {
    
        this.ipAdrress = ipAdrress;
    }
    
    public String getNickname() {
    
        return nickname;
    }
    
    public void setNickname( String nickname ) {
    
        this.nickname = nickname;
    }
    
    @Override
    public String toString() {
    
        return "Student [" + super.toString() + "ipAdrress=" + ipAdrress + ", nickname=" + nickname
                                        + ", getIpAdrress()=" + getIpAdrress() + ", getNickname()=" + getNickname()
                                        + "]";
    }
    
}
