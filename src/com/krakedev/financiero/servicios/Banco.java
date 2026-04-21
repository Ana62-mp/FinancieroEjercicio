package com.krakedev.financiero.servicios;

import com.krakedev.financiero.entidades.Cliente;
import com.krakedev.financiero.entidades.Cuenta;

public class Banco {
	private int ultimoCodigo = 1000;

	public Banco() {		
	}

	public int getUltimoCodigo() {
		return ultimoCodigo;
	}

	public void setUltimoCodigo(int ultimoCodigo) {
		this.ultimoCodigo = ultimoCodigo;
	}
	
	public Cuenta crearCuenta(Cliente cliente) {
		String codigoStr = ultimoCodigo + ""; 
		ultimoCodigo++;
		Cuenta cuenta = new Cuenta(codigoStr);
		cuenta.setPropietario(cliente);
		return cuenta;
	}
	
	
	public boolean depositar(double monto, Cuenta cuenta) {
		if(monto>0) {
			double nuevoSaldo = cuenta.getSaldoActual()+monto;
			cuenta.setSaldoActual(nuevoSaldo);
			return true;
		}else {
			return false;
		}
	}
	
	public boolean retirar(double monto, Cuenta cuenta) {
		if(monto>0 && monto<=cuenta.getSaldoActual()) {
			double nuevoSaldo = cuenta.getSaldoActual()-monto;
			cuenta.setSaldoActual(nuevoSaldo);
			return true;
		}else {
			return false;
		}
	}
	
}
