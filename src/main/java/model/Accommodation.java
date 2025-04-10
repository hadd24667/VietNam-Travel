package model;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Accommodations")
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Accommodations_ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Ten", nullable = false)
    private String ten;

    @Nationalized
    @Column(name = "DiaChi", nullable = false)
    private String diaChi;

    @Nationalized
    @Lob
    @Column(name = "MoTa", columnDefinition = "nvarchar(max)" , nullable = false)
    private String moTa;

    @Column(name = "GiaPhong", precision = 10, scale = 2, nullable = false)
    private BigDecimal giaPhong;

//    @Column(name = "SoPhongConLai", nullable = false)
//    private Integer SoPhongConLai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Images_ID")
    private Images images;

    @Column(name = "DoRongPhong", nullable = false)
    private Integer doRongPhong;

    public Integer getDoRongPhong() {
        return doRongPhong;
    }

    public void setDoRongPhong(Integer doRongPhong) {
        this.doRongPhong = doRongPhong;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(BigDecimal giaPhong) {
        this.giaPhong = giaPhong;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

}