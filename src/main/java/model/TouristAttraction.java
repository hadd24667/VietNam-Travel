package model;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "TouristAttractions")
public class TouristAttraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TouristAttractions_ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Ten", nullable = false)
    private String ten;

    @Nationalized
    @Lob
    @Column(name = "MoTa", columnDefinition = "nvarchar(max)")
    private String moTa;

    @Column(name = "GiaVe", precision = 10, scale = 2)
    private BigDecimal giaVe;

    @Nationalized
    @Column(name = "DiaChi")
    private String diaChi;

    @Nationalized
    @Column(name = "ToaDoGoogleMaps")
    private String toaDoGoogleMaps;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Images_ID")
    private Images images;

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

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(BigDecimal giaVe) {
        this.giaVe = giaVe;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getToaDoGoogleMaps() {
        return toaDoGoogleMaps;
    }

    public void setToaDoGoogleMaps(String toaDoGoogleMaps) {
        this.toaDoGoogleMaps = toaDoGoogleMaps;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

}