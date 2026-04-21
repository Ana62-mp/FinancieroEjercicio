package com.krakedev.financiero.servicios.test;

import com.krakedev.financiero.entidades.Cliente;
import com.krakedev.financiero.entidades.Cuenta;
import com.krakedev.financiero.servicios.Banco;

public class TestBanco {
	
	public static void main(String[] args) {
		Cliente cliente1 = new Cliente();
		Cliente cliente2 = new Cliente("1752724748","Ana","Mopo");
		Banco banco = new Banco();
		
		Cuenta cuenta1Creada = banco.crearCuenta(cliente1);
		cuenta1Creada.imprimir();
		
		Cuenta cuenta2Creada = banco.crearCuenta(cliente1);
		cuenta2Creada.imprimir();
		
		
	}
	
}
