import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<Menu> menuList = new ArrayList<>();
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        // Data awal
        menuList.add(new Menu("Nasi Goreng", 15000, "Makanan"));
        menuList.add(new Menu("Ayam Bakar", 30000, "Makanan"));
        menuList.add(new Menu("Sate Ayam", 20000, "Makanan"));
        menuList.add(new Menu("Mie Ayam", 15000, "Makanan"));
        menuList.add(new Menu("Es Teh Manis", 8000, "Minuman"));
        menuList.add(new Menu("Air Mineral", 5000, "Minuman"));
        menuList.add(new Menu("Es Campur", 10000, "Minuman"));
        menuList.add(new Menu("Es Jeruk", 10000, "Minuman"));

        while (true) {
            System.out.println("\n=== APLIKASI RESTORAN ===");
            System.out.println("1. Pemesanan");
            System.out.println("2. Manajemen Menu");
            System.out.println("3. Keluar");
            System.out.print("Pilih: ");

            String pilih = input.nextLine();

            switch (pilih) {
                case "1":
                    prosesPemesanan();
                    break;
                case "2":
                    menuManajemen();
                    break;
                case "3":
                    System.out.println("Terima kasih!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // ===========================
    // PEMESANAN
    // ===========================
    static void prosesPemesanan() {
        ArrayList<String> pesananNama = new ArrayList<>();
        ArrayList<Integer> pesananJumlah = new ArrayList<>();

        while (true) {
            tampilkanMenuDenganNomor();

            System.out.print("Masukkan nama/nomor menu (atau 'selesai'): ");
            String nama = input.nextLine();

            if (nama.equalsIgnoreCase("selesai"))
                break;

            Menu item = null;

            // Jika input berupa angka → anggap sebagai nomor menu
            if (nama.matches("\\d+")) {
                int nomor = Integer.parseInt(nama);
                if (nomor >= 1 && nomor <= menuList.size()) {
                    item = menuList.get(nomor - 1);
                }
            } else {
                // Jika input berupa teks → cari berdasarkan nama
                item = cariMenu(nama);
            }

            if (item == null) {
                System.out.println("Menu tidak ditemukan! Coba lagi.");
                continue;
            }

            int jumlah = 0;
            while (true) {
                try {
                    System.out.print("Jumlah: ");
                    jumlah = Integer.parseInt(input.nextLine());
                    if (jumlah > 0)
                        break;
                } catch (Exception e) {
                }
                System.out.println("Jumlah tidak valid!");
            }

            pesananNama.add(item.nama);
            pesananJumlah.add(jumlah);
        }

        cetakStruk(pesananNama, pesananJumlah);
    }

    // ===========================
    // CETAK STRUK
    // ===========================
    static void cetakStruk(ArrayList<String> pesanan, ArrayList<Integer> jumlah) {

        int subtotal = 0;
        int totalMinuman = 0;

        System.out.println("\n===== STRUK PEMBAYARAN =====");

        for (int i = 0; i < pesanan.size(); i++) {
            Menu m = cariMenu(pesanan.get(i));
            int qty = jumlah.get(i);
            int totalItem = m.harga * qty;

            System.out.println(m.nama + " x" + qty + " = Rp " + totalItem);

            subtotal += totalItem;

            if (m.kategori.equalsIgnoreCase("Minuman")) {
                totalMinuman += qty;
            }
        }

        // Promo B1G1 minuman
        int potonganMinuman = 0;
        if (subtotal > 50000 && totalMinuman >= 2) {
            potonganMinuman = hitungGratisMinuman(pesanan, jumlah);
        }

        // Diskon 10%
        int diskon = 0;
        if (subtotal > 100000) {
            diskon = subtotal / 10;
        }

        int pajak = (subtotal - diskon - potonganMinuman) / 10;
        int service = 20000;

        int totalAkhir = subtotal - diskon - potonganMinuman + pajak + service;

        System.out.println("\nSubtotal: Rp " + subtotal);
        if (diskon > 0)
            System.out.println("Diskon 10%: -Rp " + diskon);
        if (potonganMinuman > 0)
            System.out.println("Promo B1G1 Minuman: -Rp " + potonganMinuman);
        System.out.println("Pajak 10%: Rp " + pajak);
        System.out.println("Biaya Pelayanan: Rp " + service);

        System.out.println("\nTOTAL AKHIR: Rp " + totalAkhir);
    }

    static int hitungGratisMinuman(ArrayList<String> pesanan, ArrayList<Integer> jumlah) {
        int hargaTermurah = Integer.MAX_VALUE;

        for (int i = 0; i < pesanan.size(); i++) {
            Menu m = cariMenu(pesanan.get(i));
            if (m.kategori.equalsIgnoreCase("Minuman")) {
                if (m.harga < hargaTermurah)
                    hargaTermurah = m.harga;
            }
        }

        return hargaTermurah == Integer.MAX_VALUE ? 0 : hargaTermurah;
    }

    // ===========================
    // MANAJEMEN MENU
    // ===========================
    static void menuManajemen() {
        while (true) {
            System.out.println("\n=== MANAJEMEN MENU ===");
            System.out.println("1. Tambah Menu");
            System.out.println("2. Ubah Harga");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali");
            System.out.print("Pilih: ");

            String pilih = input.nextLine();

            switch (pilih) {
                case "1":
                    tambahMenu();
                    break;
                case "2":
                    ubahHarga();
                    break;
                case "3":
                    hapusMenu();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    static void tambahMenu() {
        System.out.print("Nama menu: ");
        String nama = input.nextLine();
        System.out.print("Harga: ");
        int harga = Integer.parseInt(input.nextLine());
        System.out.print("Kategori (Makanan/Minuman): ");
        String kategori = input.nextLine();

        menuList.add(new Menu(nama, harga, kategori));
        System.out.println("Menu berhasil ditambahkan.");
    }

    static void ubahHarga() {
        tampilkanMenuDenganNomor();

        int nomor = validasiNomorMenu("Masukkan nomor menu yang ingin diubah: ");

        Menu m = menuList.get(nomor - 1);

        System.out.print("Harga baru: ");
        int hargaBaru = Integer.parseInt(input.nextLine());

        System.out.print("Yakin ubah? (Ya/Tidak): ");
        if (input.nextLine().equalsIgnoreCase("Ya")) {
            m.harga = hargaBaru;
            System.out.println("Harga berhasil diubah.");
        } else {
            System.out.println("Perubahan dibatalkan.");
        }
    }

    static void hapusMenu() {
        tampilkanMenuDenganNomor();

        int nomor = validasiNomorMenu("Masukkan nomor menu yang ingin dihapus: ");

        Menu m = menuList.get(nomor - 1);

        System.out.print("Yakin hapus? (Ya/Tidak): ");
        if (input.nextLine().equalsIgnoreCase("Ya")) {
            menuList.remove(m);
            System.out.println("Menu berhasil dihapus.");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    // ===========================
    // UTILITAS
    // ===========================
    static void tampilkanMenuDenganNomor() {
        System.out.println("\n===== MENU RESTORAN =====");
        for (int i = 0; i < menuList.size(); i++) {
            Menu m = menuList.get(i);
            System.out.println((i + 1) + ". " + m.nama + " (Rp " + m.harga + ") [" + m.kategori + "]");
        }
    }

    static int validasiNomorMenu(String pesan) {
        int nomor = -1;
        while (true) {
            try {
                System.out.print(pesan);
                nomor = Integer.parseInt(input.nextLine());
                if (nomor >= 1 && nomor <= menuList.size())
                    break;
            } catch (Exception e) {
            }
            System.out.println("Nomor tidak valid! Coba lagi.");
        }
        return nomor;
    }

    static Menu cariMenu(String nama) {
        for (Menu m : menuList) {
            if (m.nama.equalsIgnoreCase(nama)) {
                return m;
            }
        }
        return null;
    }
}
