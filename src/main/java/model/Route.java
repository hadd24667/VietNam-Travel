package model;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "Routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Routes_ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "DiemKhoiHanh", nullable = false)
    private String diemKhoiHanh;

    @Nationalized
    @Column(name = "DiemDen", nullable = false)
    private String diemDen;

    @Nationalized
    @Column(name = "LoaiPhuongTien", nullable = false, length = 50)
    private String loaiPhuongTien;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiemKhoiHanh() {
        return diemKhoiHanh;
    }

    public void setDiemKhoiHanh(String diemKhoiHanh) {
        this.diemKhoiHanh = diemKhoiHanh;
    }

    public String getDiemDen() {
        return diemDen;
    }

    public void setDiemDen(String diemDen) {
        this.diemDen = diemDen;
    }

    public String getLoaiPhuongTien() {
        return loaiPhuongTien;
    }

    public void setLoaiPhuongTien(String loaiPhuongTien) {
        this.loaiPhuongTien = loaiPhuongTien;
    }

}