package id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Model;

public class Rider {
    private String email, password, nama, phone, AvatarUrl, Umur, Alamat, JenisKelamin, KK, KTP, Asal, Jabatan, Nip;

    public Rider() {
    }

    public Rider(String email, String password, String nama, String phone, String avatarUrl, String umur, String alamat, String jenisKelamin, String KK, String KTP, String asal, String jabatan, String nip) {
        this.email = email;
        this.password = password;
        this.nama = nama;
        this.phone = phone;
        AvatarUrl = avatarUrl;
        Umur = umur;
        Alamat = alamat;
        JenisKelamin = jenisKelamin;
        this.KK = KK;
        this.KTP = KTP;
        Asal = asal;
        Jabatan = jabatan;
        Nip = nip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
    }

    public String getUmur() {
        return Umur;
    }

    public void setUmur(String umur) {
        Umur = umur;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getJenisKelamin() {
        return JenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        JenisKelamin = jenisKelamin;
    }

    public String getKK() {
        return KK;
    }

    public void setKK(String KK) {
        this.KK = KK;
    }

    public String getKTP() {
        return KTP;
    }

    public void setKTP(String KTP) {
        this.KTP = KTP;
    }

    public String getAsal() {
        return Asal;
    }

    public void setAsal(String asal) {
        Asal = asal;
    }

    public String getJabatan() {
        return Jabatan;
    }

    public void setJabatan(String jabatan) {
        Jabatan = jabatan;
    }

    public String getNip() {
        return Nip;
    }

    public void setNip(String nip) {
        Nip = nip;
    }
}
