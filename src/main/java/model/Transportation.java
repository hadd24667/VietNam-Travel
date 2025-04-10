package model;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "Transportations")
public class Transportation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Transportations_ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "TenHang", nullable = false)
    private String tenHang;

    @Nationalized
    @Column(name = "LoaiPhuongTien", nullable = false, length = 50)
    private String loaiPhuongTien;

    @OneToMany(mappedBy = "transportations")
    private Set<Schedule> schedules = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public String getLoaiPhuongTien() {
        return loaiPhuongTien;
    }

    public void setLoaiPhuongTien(String loaiPhuongTien) {
        this.loaiPhuongTien = loaiPhuongTien;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

}