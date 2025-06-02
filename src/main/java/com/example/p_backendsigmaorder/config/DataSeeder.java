package com.example.p_backendsigmaorder.config;

import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Local.repository.LocalRepository;
import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.repository.ProductoRepository;
import com.example.p_backendsigmaorder.Promocion.Repository.PromocionRepository;
import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByCorreo("admin@admin.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setCorreo("admin@admin.com");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMINISTRADOR);
            usuarioRepository.save(admin);
            System.out.println("✅ Usuario ADMIN creado: admin@admin.com / admin123");
        }

        // ✅ Crear o recuperar al encargado
        Usuario encargado = usuarioRepository.findByCorreo("encargado@local.com")
                .orElseGet(() -> {
                    Usuario nuevoEncargado = new Usuario();
                    nuevoEncargado.setNombre("Encargado Local");
                    nuevoEncargado.setCorreo("encargado@local.com");
                    nuevoEncargado.setContrasena(passwordEncoder.encode("local123"));
                    nuevoEncargado.setRol(Rol.ENCARGADO_LOCAL);
                    return usuarioRepository.save(nuevoEncargado);
                });

        // ✅ Crear o recuperar el local
        Local local = localRepository.findById(1L).orElseGet(() -> {
            Local nuevoLocal = new Local();
            nuevoLocal.setSede("TAMBOTEC");
            nuevoLocal.setDireccion("Av. Principal 123");
            nuevoLocal.setTelefono("+51987654321");
            nuevoLocal.setHorario("9am - 6pm");
            nuevoLocal.setEncargado(encargado); // <-- ahora sí existe
            return localRepository.save(nuevoLocal);
        });

        // Crear productos si no existen
        if (productoRepository.count() == 0) {
            Producto p1 = new Producto(null, "Jugo de maracuya",         "2025-08-30", "Botella de jugo natural 1L",       5.75,  80,  Categoria.Bebidas,       null, 1.0, local);
            Producto p2 = new Producto(null, "Jugo de naranja",          "2025-09-05", "Botella de jugo natural 1L",       5.50,  75,  Categoria.Bebidas,       null, 1.0, local);
            Producto p3 = new Producto(null, "Gaseosa Inca Kola",        "2025-12-31", "Botella 500ml",                    3.20, 120,  Categoria.Bebidas,       null, 0.5, local);
            Producto p4 = new Producto(null, "Gaseosa Coca-Cola",        "2025-12-31", "Botella 500ml",                    3.00, 110,  Categoria.Bebidas,       null, 0.5, local);
            Producto p5 = new Producto(null, "Agua San Luis",            "2026-06-30", "Botella 500ml",                    1.50, 200,  Categoria.Bebidas,       null, 0.5, local);
            Producto p6 = new Producto(null, "Leche evaporada Gloria",   "2026-01-10", "Lata 400g",                        4.00,  90,  Categoria.Lacteos,        null, 0.4, local);
            Producto p7 = new Producto(null, "Yogurt bebible",           "2025-11-20", "Envase 250ml",                     2.80,  60,  Categoria.Lacteos,        null, 0.3, local);
            Producto p8 = new Producto(null, "Pan de molde",             "2025-06-15", "Paquete 500g",                     4.50, 100,  Categoria.Panaderia,     null, 0.5, local);
            Producto p9 = new Producto(null, "Harina sin preparar",      "2026-02-01", "Bolsa 1kg",                        3.20,  50,  Categoria.Abarrotes,     null, 1.0, local);
            Producto p10 = new Producto(null,"Arroz Extra",              "2026-03-10", "Bolsa 1kg",                        4.00, 150,  Categoria.Abarrotes,     null, 1.0, local);
            Producto p11 = new Producto(null,"Azúcar blanca",           "2026-04-20", "Bolsa 1kg",                        3.00, 120,  Categoria.Abarrotes,     null, 1.0, local);
            Producto p12 = new Producto(null,"Sal yodada",              "2026-05-05", "Paquete 500g",                     1.80, 100,  Categoria.Abarrotes,     null, 0.5, local);
            Producto p13 = new Producto(null,"Aceite Primor",           "2026-01-15", "Botella 1L",                       12.50,  70, Categoria.Abarrotes,     null, 1.0, local);
            Producto p14 = new Producto(null,"Fideo Tallarín",          "2026-01-15", "Paquete 500g",                     3.20, 100,  Categoria.Abarrotes,     null, 0.5, local);
            Producto p15 = new Producto(null,"Puré de papa instantáneo","2025-10-30", "Caja 125g",                        2.50,  80,  Categoria.Abarrotes,     null, 0.2, local);
            Producto p16 = new Producto(null,"Atún en lata",            "2026-02-28", "Lata 170g",                       6.00,  65,  Categoria.Abarrotes,     null, 0.2, local);
            Producto p17 = new Producto(null,"Salsa de soya",           "2025-12-01", "Botella 250ml",                   4.50,  60,  Categoria.Abarrotes,     null, 0.3, local);
            Producto p18 = new Producto(null,"Salsa de huancaína",      "2025-11-30", "Sobre 50g",                       1.50,  90,  Categoria.Abarrotes,     null, 0.05,local);
            Producto p19 = new Producto(null,"Ají amarillo pasta",      "2025-10-15", "Tarro 100g",                      3.80,  50,  Categoria.Abarrotes,     null, 0.1, local);
            Producto p20 = new Producto(null,"Sopa instantánea",        "2026-01-20", "Sobre 80g",                       2.20, 110,  Categoria.Abarrotes,     null, 0.08,local);
            Producto p21 = new Producto(null, "Café instantáneo",           "2026-06-01", "Frasco 100g",            8.00,  55,  Categoria.Abarrotes,     null, 0.1,  local);
            Producto p22 = new Producto(null, "Chifles",                    "2026-02-15", "Bolsa 120g",            4.20,  70,  Categoria.Snacks,       null, 0.12, local);
            Producto p23 = new Producto(null, "Papas Lay's",                "2026-03-05", "Bolsa 110g",            4.00,  75,  Categoria.Snacks,       null, 0.11, local);
            Producto p24 = new Producto(null, "Inka Chips",                 "2026-03-10", "Bolsa 100g",            3.80,  60,  Categoria.Snacks,       null, 0.10, local);
            Producto p25 = new Producto(null, "Galletas Oreo",              "2026-04-01", "Paquete 96g",           5.00,  80,  Categoria.Snacks,       null, 0.10, local);
            Producto p26 = new Producto(null, "Galletas Casino",            "2026-04-10", "Paquete 80g",           4.50,  60,  Categoria.Snacks,       null, 0.08, local);
            Producto p27 = new Producto(null, "Chocolates Sublime",         "2026-05-01", "Barra 90g",             3.50,  90,  Categoria.Snacks,       null, 0.09, local);
            Producto p28 = new Producto(null, "Chocolates Princesa",        "2026-05-05", "Barra 70g",             3.00,  85,  Categoria.Snacks,       null, 0.07, local);
            Producto p29 = new Producto(null, "Chocolates Triángulo",       "2026-05-10", "Paquete 20g",           1.80, 100,  Categoria.Snacks,       null, 0.02, local);
            Producto p30 = new Producto(null, "Galletas María",             "2026-06-01", "Paquete 100g",          3.20,  70,  Categoria.Snacks,       null, 0.10, local);
            Producto p31 = new Producto(null, "Galletas Ritz",              "2026-06-05", "Paquete 80g",           4.00,  65,  Categoria.Snacks,       null, 0.08, local);
            Producto p32 = new Producto(null, "Polvo de hornear",           "2026-04-10", "Paquete 100g",          2.20,  40,  Categoria.Abarrotes,     null, 0.10, local);
            Producto p33 = new Producto(null, "Esencia de vainilla",        "2026-05-05", "Frasco 50ml",           3.50,  30,  Categoria.Abarrotes,     null, 0.05, local);
            Producto p34 = new Producto(null, "Mermelada de fresa",         "2026-02-28", "Frasco 250g",           5.20,  45,  Categoria.Abarrotes,     null, 0.30, local);
            Producto p35 = new Producto(null, "Miel de abeja",              "2026-03-15", "Botella 350g",          8.00,  40,  Categoria.Abarrotes,     null, 0.35, local);
            Producto p36 = new Producto(null, "Mayonesa",                   "2026-04-10", "Tarro 250g",            4.30,  70,  Categoria.Abarrotes,     null, 0.25, local);
            Producto p37 = new Producto(null, "Salsa kétchup",              "2026-05-05", "Botella 500g",          4.50,  65,  Categoria.Abarrotes,     null, 0.50, local);
            Producto p38 = new Producto(null, "Chocolate en polvo",         "2026-03-01", "Paquete 200g",          6.50,  40,  Categoria.Abarrotes,     null, 0.20, local);
            Producto p39 = new Producto(null, "Canelitas",                  "2026-04-01", "Paquete 75g",           3.50,  80,  Categoria.Snacks,       null, 0.08, local);
            Producto p40 = new Producto(null, "Galletas Premium",           "2026-07-01", "Paquete 100g",          6.00,  50,  Categoria.Snacks,       null, 0.10, local);
            Producto p41 = new Producto(null, "Bebida isotónica Gatorade", "2026-03-20", "Botella 600ml",         7.50,  60, Categoria.Bebidas,      null, 0.6,  local);
            Producto p42 = new Producto(null, "Panetón",                  "2026-01-01", "Panetón 500g",           15.00, 30, Categoria.Panaderia,   null, 0.5,  local);
            Producto p43 = new Producto(null, "Chocolate en barra",       "2026-07-01", "Barra 100g",             4.00,  80, Categoria.Snacks,       null, 0.1,  local);
            Producto p44 = new Producto(null, "Cereal de maíz",           "2026-08-01", "Caja 250g",              7.00,  50, Categoria.Abarrotes,   null, 0.25, local);
            Producto p45 = new Producto(null, "Cereal avena con frutas",  "2026-09-01", "Caja 300g",              8.50,  45, Categoria.Abarrotes,   null, 0.3,  local);
            Producto p46 = new Producto(null, "Galleta de soda",          "2026-06-15", "Paquete 80g",            3.00,  70, Categoria.Snacks,       null, 0.08, local);
            Producto p47 = new Producto(null, "Galletas Festival",        "2026-03-10", "Paquete 68g",            4.00,  70, Categoria.Snacks,       null, 0.07, local);
            Producto p48 = new Producto(null, "Barra de granola",         "2026-04-20", "Unidad 25g",             2.50,  80, Categoria.Varios,       null, 0.03, local);
            Producto p49 = new Producto(null, "Sopa de pollo en lata",    "2026-02-01", "Lata 300g",              7.00,  50, Categoria.Abarrotes,   null, 0.3,  local);
            Producto p50 = new Producto(null, "Atún en agua",             "2026-02-28", "Lata 170g",              6.50,  60, Categoria.Abarrotes,   null, 0.2,  local);
            Producto p51 = new Producto(null, "Leche en polvo",           "2026-11-01", "Bolsa 500g",             9.00,  50, Categoria.Lacteos,     null, 0.5,  local);
            Producto p52 = new Producto(null, "Yogurt griego",            "2025-11-15", "Envase 150g",            3.00,  45, Categoria.Lacteos,     null, 0.15, local);
            Producto p53 = new Producto(null, "Queso crema",              "2026-01-20", "Frasco 200g",            6.50,  35, Categoria.Lacteos,     null, 0.2,  local);
            Producto p54 = new Producto(null, "Jamón de pavo",            "2026-02-28", "Paquete 200g",           12.00, 30, Categoria.Lacteos,     null, 0.2,  local);
            Producto p55 = new Producto(null, "Chips de camote",          "2026-02-15", "Bolsa 100g",             4.00,  65, Categoria.Snacks,       null, 0.1,  local);
            Producto p56 = new Producto(null, "Chips de choclo",          "2026-03-01", "Bolsa 100g",             4.20,  60, Categoria.Snacks,       null, 0.1,  local);
            Producto p57 = new Producto(null, "Empanada de queso",        "2025-12-05", "Unidad",                 2.00,  60, Categoria.Panaderia,   null, 0.2,  local);
            Producto p58 = new Producto(null, "Pan francés",              "2025-12-05", "Unidad",                 0.50, 100, Categoria.Panaderia,   null, 0.15, local);
            Producto p59 = new Producto(null, "Rosquilla",                "2025-11-20", "Paquete 6 unidades",     4.50,  40, Categoria.Panaderia,   null, 0.3,  local);
            Producto p60 = new Producto(null, "Pan de yema",              "2025-12-01", "Paquete 200g",           5.00,  50, Categoria.Panaderia,   null, 0.2,  local);
            Producto p61 = new Producto(null, "Detergente Bolívar",       "2026-01-01", "Bolsa 1kg",               8.50,  60, Categoria.Limpieza,     null, 1.0, local);
            Producto p62 = new Producto(null, "Lavavajilla Sapolio",       "2026-01-15", "Botella 500ml",           5.50,  70, Categoria.Limpieza,     null, 0.5, local);
            Producto p63 = new Producto(null, "Jabón de tocador",          "2026-02-01", "Pack 3 unidades",        4.20,  80, Categoria.Limpieza,     null, 0.15,local);
            Producto p64 = new Producto(null, "Papel higiénico",           "2026-06-30", "Pack 6 rollos",          12.00,  90, Categoria.Limpieza,     null, 0.8, local);
            Producto p65 = new Producto(null, "Shampoo Savital",           "2026-02-20", "Botella 250ml",           6.80,  55, Categoria.Aseo_personal,null, 0.3, local);
            Producto p66 = new Producto(null, "Crema dental Kolynos",      "2026-03-01", "Tubito 100g",             3.20,  75, Categoria.Aseo_personal,null, 0.1, local);
            Producto p67 = new Producto(null, "Cepillo dental",            "2026-04-01", "Unidad",                  2.50,  65, Categoria.Aseo_personal,null, 0.05,local);
            Producto p68 = new Producto(null, "Toallas húmedas",           "2026-05-01", "Pack 20 unidades",       10.00,  55, Categoria.Aseo_personal,null, 0.5, local);
            Producto p69 = new Producto(null, "Alcohol en gel",            "2026-05-10", "Frasco 250ml",            5.50,  80, Categoria.Limpieza,     null, 0.25,local);
            Producto p70 = new Producto(null, "Suavizante ropa",           "2026-03-15", "Botella 1L",              8.00,  50, Categoria.Limpieza,     null, 1.0,  local);
            Producto p71 = new Producto(null, "Pañales para bebé",         "2026-06-01", "Pack 20 unidades",       35.00,  30, Categoria.Varios,       null, 0.5,  local);
            Producto p72 = new Producto(null, "Toallas sanitarias",        "2026-07-01", "Pack 10 unidades",       12.00,  40, Categoria.Varios,       null, 0.3,  local);
            Producto p73 = new Producto(null, "Cigarrillos Panama",        "2026-12-31", "Pack 20",                12.00,  50, Categoria.Cigarrillos,  null, 0.03,local);
            Producto p74 = new Producto(null, "Cigarrillos Marlboro",      "2026-12-31", "Pack 20",                14.00,  45, Categoria.Cigarrillos,  null, 0.03,local);
            Producto p75 = new Producto(null, "Galleta soda",              "2026-06-15", "Paquete 80g",             3.00,  70, Categoria.Snacks,       null, 0.08,local);
            Producto p76 = new Producto(null, "Cereal de maíz",            "2026-08-01", "Caja 250g",               7.00,  50, Categoria.Abarrotes,    null, 0.25,local);
            Producto p77 = new Producto(null, "Galletas Ritz",             "2026-06-05", "Paquete 80g",             4.00,  65, Categoria.Snacks,       null, 0.08,local);
            Producto p78 = new Producto(null, "Barra energética",          "2026-04-01", "Unidad 50g",             3.50,  55, Categoria.Snacks,       null, 0.05,local);
            Producto p79 = new Producto(null, "Chocolate dietético",       "2026-08-01", "Barra 70g",               3.50,  55, Categoria.Snacks,       null, 0.07,local);
            Producto p80 = new Producto(null, "Bebida energizante Volt",   "2026-06-20", "Lata 250ml",              7.20,  60, Categoria.Bebidas,      null, 0.25,local);
            Producto p81 = new Producto(null, "Galletas surtidas",            "2026-06-15", "Paquete 100g",          5.50,  60, Categoria.Snacks,       null, 0.1,  local);
            Producto p82 = new Producto(null, "Chocolate caliente en polvo",   "2026-03-01", "Paquete 200g",          7.00,  40, Categoria.Abarrotes,     null, 0.2,  local);
            Producto p83 = new Producto(null, "Gaseosa Fanta",                 "2025-12-31", "Botella 500ml",         3.00,  90, Categoria.Bebidas,      null, 0.5,  local);
            Producto p84 = new Producto(null, "Café molido",                   "2026-07-01", "Paquete 250g",         10.00,  45, Categoria.Abarrotes,     null, 0.25, local);
            Producto p85 = new Producto(null, "Chips de quinua",               "2026-05-10", "Bolsa 100g",            4.50,  55, Categoria.Snacks,       null, 0.1,  local);
            Producto p86 = new Producto(null, "Panetón Bimbo",                 "2026-01-01", "Panetón 1kg",           25.00, 20, Categoria.Panaderia,     null, 1.0,  local);
            Producto p87 = new Producto(null, "Mantequilla",                   "2026-04-15", "Tarro 200g",            7.50,  50, Categoria.Lacteos,       null, 0.2,  local);
            Producto p88 = new Producto(null, "Crema de mani",                  "2026-06-01", "Tarro 500g",            12.00, 30, Categoria.Abarrotes,     null, 0.5,  local);
            Producto p89 = new Producto(null, "Salsa BBQ",                     "2026-07-10", "Botella 300g",           6.00,  40, Categoria.Abarrotes,     null, 0.3,  local);
            Producto p90 = new Producto(null, "Galleta de chocolate",          "2026-08-01", "Paquete 90g",           4.00,  70, Categoria.Snacks,       null, 0.09, local);
            Producto p91 = new Producto(null, "Detergente líquido Ace",        "2026-02-10", "Botella 1L",            10.00, 50, Categoria.Limpieza,     null, 1.0,  local);
            Producto p92 = new Producto(null, "Limpiador de pisos",            "2026-03-05", "Botella 1L",             6.50,  60, Categoria.Limpieza,     null, 1.0,  local);
            Producto p93 = new Producto(null, "Esponjas de cocina",             "2026-04-01", "Pack 5 unidades",        3.00,  80, Categoria.Limpieza,     null, 0.2,  local);
            Producto p94 = new Producto(null, "Guantes de limpieza",           "2026-05-01", "Par 1 unidad",           2.50, 100, Categoria.Limpieza,     null, 0.1,  local);
            Producto p95 = new Producto(null, "Papel aluminio",                "2026-06-01", "Rollo 20m",              5.00,  70, Categoria.Varios,       null, 0.3,  local);
            Producto p96 = new Producto(null, "Film transparente",             "2026-07-01", "Rollo 30m",              4.50,  65, Categoria.Varios,       null, 0.2,  local);
            Producto p97 = new Producto(null, "Cigarrillos Derby",             "2026-12-31", "Pack 20 unidades",      10.00, 60, Categoria.Cigarrillos,  null, 0.03, local);
            Producto p98 = new Producto(null, "Chocolate relleno",             "2026-08-01", "Paquete 150g",           6.00,  50, Categoria.Snacks,       null, 0.15, local);
            Producto p99 = new Producto(null, "Galleta de avena integral",      "2026-09-01", "Paquete 120g",           4.80,  60, Categoria.Snacks,       null, 0.12, local);
            Producto p100 = new Producto(null,"Barra de cereal con chocolate", "2026-10-01", "Unidad 40g",            3.80,  55, Categoria.Snacks,       null, 0.04, local);


            productoRepository.saveAll(Arrays.asList(
                    p1,  p2,  p3,  p4,  p5,  p6,  p7,  p8,  p9,  p10,
                    p11, p12, p13, p14, p15, p16, p17, p18, p19, p20,
                    p21, p22, p23, p24, p25, p26, p27, p28, p29, p30,
                    p31, p32, p33, p34, p35, p36, p37, p38, p39, p40,
                    p41, p42, p43, p44, p45, p46, p47, p48, p49, p50,
                    p51, p52, p53, p54, p55, p56, p57, p58, p59, p60,
                    p61, p62, p63, p64, p65, p66, p67, p68, p69, p70,
                    p71, p72, p73, p74, p75, p76, p77, p78, p79, p80,
                    p81, p82, p83, p84, p85, p86, p87, p88, p89, p90,
                    p91, p92, p93, p94, p95, p96, p97, p98, p99, p100
            ));

            // Solo si no hay promociones cargadas
            //==============PROMOCIONES========================
            if (promocionRepository.count() == 0) {
                // Obtén algunos productos para vincular a las promociones
                Producto fideo = productoRepository.findByNombreContainingIgnoreCase("Fideo Tallarín").stream().findFirst().orElse(null);
                Producto salsaSoya = productoRepository.findByNombreContainingIgnoreCase("Salsa de soya").stream().findFirst().orElse(null);

                if (fideo != null && salsaSoya != null ) {
                    Promocion promoTallarín = new Promocion();
                    promoTallarín.setCodigoPromocion("TALLARIN10");
                    promoTallarín.setNombre("Promo Tallarín Saltado 10%");
                    promoTallarín.setDescripcion("10% de descuento en fideos, salsa de soya ");
                    promoTallarín.setFechaInicio(LocalDate.now());
                    promoTallarín.setFechaFin(LocalDate.now().plusMonths(3));
                    promoTallarín.setPorcentajeDescuento(10.0);
                    promoTallarín.setProductos(Arrays.asList(fideo, salsaSoya));

                    promocionRepository.save(promoTallarín);
                    System.out.println("✅ Promoción tallarín creada.");
                }

                // Crea más promociones según necesites
            }
    }
}
}


