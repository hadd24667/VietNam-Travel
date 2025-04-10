package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "StayBookings")
public class StayBookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Stay_ID", nullable = false)
    private Integer stayID;

    @Column(name = "CCCD", nullable = false)
    private String cccd;

    @Column(name = "SoLuongPhong", nullable = false)
    private Integer soLuongPhong;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Accommodations_ID", nullable = false)
    private Accommodation accommodationID;

    @Column(name = "TongGiaPhong", nullable = false)
    private Double tongGiaPhong;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Users_ID", nullable = false)
    private User userID;

    @Column(name = "NgayVe", nullable = false)
    private Date ngayVe;

    @Column(name = "NgayDi", nullable = false)
    private Date ngayDi;

    public Integer getId() {
        return stayID;
    }

    public void setId(Integer stayID) {
        this.stayID = stayID;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public Integer getSoLuongPhong() {
        return soLuongPhong;
    }

    public void setSoLuongPhong(Integer soLuongPhong) {
        this.soLuongPhong = soLuongPhong;
    }

    public Accommodation getAccommodationID() {
        return accommodationID;
    }

    public void setAccommodationID(Accommodation accommodationID) {
        this.accommodationID = accommodationID;
    }


    public Double getTongGiaPhong() {
        return tongGiaPhong;
    }

    public void setTongGiaPhong(Double tongGiaPhong) {
        this.tongGiaPhong = tongGiaPhong;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public Date getNgayve() {
        return ngayVe;
    }

    public void setNgayVe(Date ngayDen) {
        this.ngayVe = ngayDen;
    }

    public Date getNgayDi() {
        return ngayDi;
    }

    public void setNgayDi(Date ngayDi) {
        this.ngayDi = ngayDi;
    }

}