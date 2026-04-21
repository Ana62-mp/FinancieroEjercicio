package com.krakedev.financiero.servicios.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.krakedev.financiero.entidades.Cliente;
import com.krakedev.financiero.entidades.Cuenta;
import com.krakedev.financiero.servicios.Banco;

public class TestBancoJUnit {

	@Test
	public void testCrearCuentaCasoCorrecto() {
		// Valida que al crear una cuenta:
		// - se genere con el código inicial esperado
		// - el propietario sea el cliente recibido
		// - el saldo inicial sea 0
		// - el tipo inicial sea "A"
		// - el último código del banco avance
		
		// Arrange
		Banco banco = new Banco();
		Cliente cliente = new Cliente("1723456789", "Ana", "Perez");

		// Act
		Cuenta cuenta = banco.crearCuenta(cliente);

		// Assert
		assertNotNull(cuenta);
		assertEquals("1000", cuenta.getId());
		assertEquals(cliente, cuenta.getPropietario());
		assertEquals(0.0, cuenta.getSaldoActual(), 0.0001);
		assertEquals("A", cuenta.getTipo());
		assertEquals(1001, banco.getUltimoCodigo());
	}

	@Test
	public void testCrearCuentaConUltimoCodigoModificado() {
		// Valida que si el último código del banco cambia,
		// la nueva cuenta use ese valor como identificador
		// y luego el contador se incremente.
		
		// Arrange
		Banco banco = new Banco();
		banco.setUltimoCodigo(2000);
		Cliente cliente = new Cliente("1102345678", "Luis", "Mora");

		// Act
		Cuenta cuenta = banco.crearCuenta(cliente);

		// Assert
		assertNotNull(cuenta);
		assertEquals("2000", cuenta.getId());
		assertEquals(cliente, cuenta.getPropietario());
		assertEquals(2001, banco.getUltimoCodigo());
	}

	@Test
	public void testCrearCuentaConClienteVacio() {
		// Valida que se pueda crear una cuenta con un cliente construido
		// con el constructor vacío, y que igual quede asociado como propietario.
		
		// Arrange
		Banco banco = new Banco();
		Cliente cliente = new Cliente();

		// Act
		Cuenta cuenta = banco.crearCuenta(cliente);

		// Assert
		assertNotNull(cuenta);
		assertEquals(cliente, cuenta.getPropietario());
		assertEquals("1000", cuenta.getId());
		assertEquals(0.0, cuenta.getSaldoActual(), 0.0001);
	}

	@Test
	public void testDepositarMontoPositivo() {
		// Valida el caso correcto:
		// si se deposita un monto positivo, el depósito debe realizarse
		// y el saldo debe aumentar en ese valor.
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C001");

		// Act
		boolean resultado = banco.depositar(150.0, cuenta);

		// Assert
		assertTrue(resultado);
		assertEquals(150.0, cuenta.getSaldoActual(), 0.0001);
	}

	@Test
	public void testDepositarMontoPositivoConSaldoPrevio() {
		// Valida que al depositar en una cuenta que ya tiene saldo,
		// el nuevo saldo sea la suma del saldo anterior más el depósito.
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C002");
		cuenta.setSaldoActual(200.0);

		// Act
		boolean resultado = banco.depositar(50.0, cuenta);

		// Assert
		assertTrue(resultado);
		assertEquals(250.0, cuenta.getSaldoActual(), 0.0001);
	}

	@Test
	public void testDepositarMontoMinimoPositivo() {
		// Valida un valor límite válido:
		// un monto apenas mayor que cero sí debe depositarse.
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C003");

		// Act
		boolean resultado = banco.depositar(0.0001, cuenta);

		// Assert
		assertTrue(resultado);
		assertEquals(0.0001, cuenta.getSaldoActual(), 0.0001);
	}

	@Test
	public void testDepositarCero() {
		// Valida un valor límite inválido:
		// si el monto es 0, no debe realizarse el depósito
		// y el saldo debe permanecer igual.
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C004");
		cuenta.setSaldoActual(80.0);

		// Act
		boolean resultado = banco.depositar(0.0, cuenta);

		// Assert
		assertFalse(resultado);
		assertEquals(80.0, cuenta.getSaldoActual(), 0.0001);
	}

	@Test
	public void testDepositarMontoNegativo() {
		// Valida una entrada inválida:
		// si el monto es negativo, no debe realizarse el depósito
		// y el saldo no debe cambiar.
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C005");
		cuenta.setSaldoActual(120.0);

		// Act
		boolean resultado = banco.depositar(-25.0, cuenta);

		// Assert
		assertFalse(resultado);
		assertEquals(120.0, cuenta.getSaldoActual(), 0.0001);
	}

	@Test
	public void testDepositarNoOcurreAccionCuandoMontoInvalido() {
		// Valida específicamente el comportamiento cuando no ocurre la acción:
		// ante un monto inválido, el método debe devolver false
		// y la cuenta debe quedar exactamente igual.
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C006");
		cuenta.setSaldoActual(300.0);

		// Act
		boolean resultado = banco.depositar(-1.0, cuenta);

		// Assert
		assertFalse(resultado);
		assertEquals(300.0, cuenta.getSaldoActual(), 0.0001);
		assertEquals("C006", cuenta.getId());
		assertEquals("A", cuenta.getTipo());
	}
}