package model;
import javax.persistence.*;

@Entity
@Table(name = "Pay")
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Pay_ID")
    private int payId;

    @Column(name = "PhuongThucThanhToan", nullable = false)
    private String phuongThucThanhToan;

    @Column(name = "TrangThaiThanhToan", nullable = false)
    private String trangThaiThanhToan;

    @ManyToOne
    @JoinColumn(name = "Stay_ID", nullable = false)
    private StayBookings stayBookings;

    @ManyToOne
    @JoinColumn(name = "TourBookings_ID", nullable = false)
    private TourBooking tourBookings;

    @ManyToOne
    @JoinColumn(name = "TransportBookings_ID", nullable = false)
    private TransportBooking bookings;

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public StayBookings getStayBookings() {
        return stayBookings;
    }

    public void setStayBookings(StayBookings stayBookings) {
        this.stayBookings = stayBookings;
    }

    public TourBooking getTourBookings() {
        return tourBookings;
    }

    public void setTourBookings(TourBooking tourBookings) {
        this.tourBookings = tourBookings;
    }

    public TransportBooking getBookings() {
        return bookings;
    }

    public void setBookings(TransportBooking bookings) {
        this.bookings = bookings;
    }
}
