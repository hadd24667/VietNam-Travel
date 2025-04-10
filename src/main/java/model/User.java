package model;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Users_ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "HoTen", nullable = false)
    private String hoTen;

    @Column(name = "SoDienThoai", nullable = false, length = 20)
    private String soDienThoai;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "MatKhau", nullable = false)
    private String matKhau;

    @OneToMany(mappedBy = "users")
    private Set<TransportBooking> transportBookings = new LinkedHashSet<>();

    @Column(name = "AD", length = 5)
    private String ad;

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public Set<TransportBooking> getBookings() {
        return transportBookings;
    }

    public void setBookings(Set<TransportBooking> transportBookings) {
        this.transportBookings = transportBookings;
    }

}