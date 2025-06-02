package com.example.p_backendsigmaorder.Promocion.Exception;


public class PromocionException extends RuntimeException {

    public PromocionException(String mensaje) {
        super(mensaje);
    }

public class ProductoNoEncontradoException extends PromocionException {
    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

public class CodigoPromocionDuplicadoException extends PromocionException {
    public CodigoPromocionDuplicadoException(String mensaje) {
        super(mensaje);
    }
}

public class DatosInvalidosException extends PromocionException {
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}

public class PromocionNoEncontradaException extends PromocionException {
    public PromocionNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}

public class PorcentajeDescuentoInvalidoException extends PromocionException {
    public PorcentajeDescuentoInvalidoException(String mensaje) {
        super(mensaje);
    }
}

public class FechaInvalidaException extends PromocionException {
    public FechaInvalidaException(String mensaje) {
        super(mensaje);
    }
}

public class ProductosVaciosException extends PromocionException {
    public ProductosVaciosException(String mensaje) {
        super(mensaje);
    }
}
}