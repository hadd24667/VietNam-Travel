package model;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "Images")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Images_ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "TenFile", nullable = false)
    private String tenFile;

    @Nationalized
    @Column(name = "LoaiFile", nullable = false, length = 50)
    private String loaiFile;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "DuLieuAnh", nullable = false)
    private byte[] duLieuAnh;

    @OneToMany(mappedBy = "images")
    private Set<Accommodation> accommodations = new LinkedHashSet<>();


    @OneToMany(mappedBy = "images")
    private Set<TouristAttraction> touristAttractions = new LinkedHashSet<>();

    public Set<TouristAttraction> getTouristAttractions() {
        return touristAttractions;
    }

    public void setTouristAttractions(Set<TouristAttraction> touristAttractions) {
        this.touristAttractions = touristAttractions;
    }

    public Set<Accommodation> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(Set<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenFile() {
        return tenFile;
    }

    public void setTenFile(String tenFile) {
        this.tenFile = tenFile;
    }

    public String getLoaiFile() {
        return loaiFile;
    }

    public void setLoaiFile(String loaiFile) {
        this.loaiFile = loaiFile;
    }

    public byte[] getDuLieuAnh() {
        return duLieuAnh;
    }

    public void setDuLieuAnh(byte[] duLieuAnh) {
        this.duLieuAnh = duLieuAnh;
    }

}