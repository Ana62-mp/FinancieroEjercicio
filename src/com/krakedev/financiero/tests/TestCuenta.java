package com.krakedev.financiero.tests;

import com.krakedev.financiero.entidades.Cuenta;

public class TestCuenta {
	public static void main(String[] args) {
		Cuenta cuenta1 = new Cuenta("1475");
		cuenta1.imprimir();
	}
}
