package model;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "TourBookings")
public class TourBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TourBookings_ID")
    private int tourBookingsId;

    @ManyToOne
    @JoinColumn(name = "TouristAttractions_ID", nullable = false)
    private TouristAttraction touristAttractions;

    @ManyToOne
    @JoinColumn(name = "Users_ID", nullable = false)
    private User users;

    @Column(name = "EmailNhanVe", nullable = false)
    private String emailNhanVe;

    @Column(name = "NgayThamQuan", nullable = false)
    private LocalDate ngayThamQuan;

    @Column(name = "SoVe", nullable = false)
    private int soVe;

    @Column(name = "CCCD", nullable = false)
    private String cccd;

    @Column(name = "TotalPayment", nullable = false)
    private Double totalPayment;

    @Column(name = "SoVeTreEm")
    private Integer soVeTreEm;

    public Integer getSoVeTreEm() {
        return soVeTreEm;
    }

    public void setSoVeTreEm(Integer soVeTreEm) {
        this.soVeTreEm = soVeTreEm;
    }

    public Double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    //ten cho tham quan, ngay, ten nguoi dat ve, so dien thoai, email nhan ve, so ve nguoi lon, so ve tre em, tong tien ve
    public int getTourBookingsId() {
        return tourBookingsId;
    }

    public void setTourBookingsId(int tourBookingsId) {
        this.tourBookingsId = tourBookingsId;
    }

    public TouristAttraction getTouristAttractions() {
        return touristAttractions;
    }

    public void setTouristAttractions(TouristAttraction touristAttractions) {
        this.touristAttractions = touristAttractions;
    }
    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public String getEmailNhanVe() {
        return emailNhanVe;
    }

    public void setEmailNhanVe(String emailNhanVe) {
        this.emailNhanVe = emailNhanVe;
    }

    public LocalDate getNgayThamQuan() {
        return ngayThamQuan;
    }

    public void setNgayThamQuan(LocalDate ngayThamQuan) {
        this.ngayThamQuan = ngayThamQuan;
    }

    public int getSoVe() {
        return soVe;
    }

    public void setSoVe(int soVe) {
        this.soVe = soVe;
    }
}
