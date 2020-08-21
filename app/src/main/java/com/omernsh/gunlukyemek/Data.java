package com.omernsh.gunlukyemek;

public class Data {

    String bayii_kodu;
    String sıcak;

    public String getBayii_kodu() {
        return bayii_kodu;
    }

    public void setBayii_kodu(String bayii_kodu) {
        this.bayii_kodu = bayii_kodu;
    }

    public String getSıcak() {
        return sıcak;
    }

    public void setSıcak(String sıcak) {
        this.sıcak = sıcak;
    }

    public String getSoguk() {
        return soguk;
    }

    public void setSoguk(String soguk) {
        this.soguk = soguk;
    }

    String soguk;

    public Data(String bayii_kodu, String sıcak, String soguk) {
        this.bayii_kodu = bayii_kodu;
        this.sıcak = sıcak;
        this.soguk = soguk;
    }
}
