package com.krakedev.financiero.servicios.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.krakedev.financiero.entidades.Cliente;
import com.krakedev.financiero.entidades.Cuenta;
import com.krakedev.financiero.servicios.Banco;

public class TestBancoJUnit {
	
	@DisplayName("crearCuenta(): genera cuenta con código inicial, saldo 0 y cliente propietario")
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
	
	@DisplayName("crearCuenta(): respeta último código modificado y lo incrementa")
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
	
	@DisplayName("crearCuenta(): permite crear cuenta con cliente vacío")
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
	
	@DisplayName("depositar(): realiza depósito correctamente con monto positivo")
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
	
	@DisplayName("depositar(): suma correctamente cuando ya existe saldo previo")
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
	
	@DisplayName("depositar(): acepta monto mínimo positivo cercano a cero")
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
	
	@DisplayName("depositar(): rechaza monto igual a cero")
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

	@DisplayName("depositar(): rechaza montos negativos")
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
	
	@DisplayName("depositar(): no modifica la cuenta cuando el monto es inválido")
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
	
	@DisplayName("retirar(): descuenta saldo correctamente cuando el monto es válido")
	@Test
	public void testRetirarCasoCorrecto() {
		// Valida que al retirar un monto válido menor al saldo
		// el retiro se realice y el saldo disminuya correctamente
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C001");
		cuenta.setSaldoActual(200);

		// Act
		boolean resultado = banco.retirar(50, cuenta);

		// Assert
		assertTrue(resultado);
		assertEquals(150, cuenta.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("retirar(): permite retirar exactamente todo el saldo disponible")
	@Test
	public void testRetirarTodoElSaldo() {
		// Valida que se pueda retirar exactamente todo el saldo
		// y la cuenta quede en cero
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C002");
		cuenta.setSaldoActual(100);

		// Act
		boolean resultado = banco.retirar(100, cuenta);

		// Assert
		assertTrue(resultado);
		assertEquals(0, cuenta.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("retirar(): rechaza retiro cuando el monto supera el saldo")
	@Test
	public void testRetirarMontoMayorAlSaldo() {
		// Valida que no se permita retirar un monto mayor al saldo disponible
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C003");
		cuenta.setSaldoActual(80);

		// Act
		boolean resultado = banco.retirar(120, cuenta);

		// Assert
		assertFalse(resultado);
		assertEquals(80, cuenta.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("retirar(): rechaza retiro de monto cero")
	@Test
	public void testRetirarMontoCero() {
		// Valida que no se pueda retirar un monto igual a cero
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C004");
		cuenta.setSaldoActual(50);

		// Act
		boolean resultado = banco.retirar(0, cuenta);

		// Assert
		assertFalse(resultado);
		assertEquals(50, cuenta.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("retirar(): rechaza retiro de montos negativos")
	@Test
	public void testRetirarMontoNegativo() {
		// Valida que no se permita retirar montos negativos
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C005");
		cuenta.setSaldoActual(100);

		// Act
		boolean resultado = banco.retirar(-30, cuenta);

		// Assert
		assertFalse(resultado);
		assertEquals(100, cuenta.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("retirar(): permite retirar saldo mínimo disponible")
	@Test
	public void testRetirarSaldoMinimo() {
		// Valida un caso límite donde el saldo es muy pequeño
		// pero el retiro sigue siendo válido
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C006");
		cuenta.setSaldoActual(0.5);

		// Act
		boolean resultado = banco.retirar(0.5, cuenta);

		// Assert
		assertTrue(resultado);
		assertEquals(0, cuenta.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("retirar(): no modifica el saldo cuando el retiro falla")
	@Test
	public void testNoSeModificaSaldoCuandoFallaRetiro() {
		// Valida que si el retiro no es válido,
		// el saldo permanezca exactamente igual
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("C007");
		cuenta.setSaldoActual(300);

		// Act
		boolean resultado = banco.retirar(500, cuenta);

		// Assert
		assertFalse(resultado);
		assertEquals(300, cuenta.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("transferir(): mueve dinero correctamente entre cuentas válidas")
	@Test
	public void testTransferirCasoCorrecto() {
		// Valida que una transferencia válida reste saldo a la cuenta origen
		// y sume saldo a la cuenta destino
		
		// Arrange
		Banco banco = new Banco();
		Cuenta origen = new Cuenta("C001");
		Cuenta destino = new Cuenta("C002");
		origen.setSaldoActual(200.0);
		destino.setSaldoActual(50.0);

		// Act
		boolean resultado = banco.transferir(100.0, origen, destino);

		// Assert
		assertTrue(resultado);
		assertEquals(100.0, origen.getSaldoActual(), 0.0001);
		assertEquals(150.0, destino.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("transferir(): permite transferir exactamente todo el saldo de origen")
	@Test
	public void testTransferirMontoIgualAlSaldoOrigen() {
		// Valida que se pueda transferir exactamente todo el saldo disponible
		// de la cuenta origen
		
		// Arrange
		Banco banco = new Banco();
		Cuenta origen = new Cuenta("C003");
		Cuenta destino = new Cuenta("C004");
		origen.setSaldoActual(80.0);
		destino.setSaldoActual(20.0);

		// Act
		boolean resultado = banco.transferir(80.0, origen, destino);

		// Assert
		assertTrue(resultado);
		assertEquals(0.0, origen.getSaldoActual(), 0.0001);
		assertEquals(100.0, destino.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("transferir(): rechaza transferencia mayor al saldo disponible")
	@Test
	public void testTransferirMontoMayorAlSaldo() {
		// Valida que no se permita transferir un monto mayor al saldo
		// disponible en la cuenta origen
		
		// Arrange
		Banco banco = new Banco();
		Cuenta origen = new Cuenta("C005");
		Cuenta destino = new Cuenta("C006");
		origen.setSaldoActual(60.0);
		destino.setSaldoActual(10.0);

		// Act
		boolean resultado = banco.transferir(100.0, origen, destino);

		// Assert
		assertFalse(resultado);
		assertEquals(60.0, origen.getSaldoActual(), 0.0001);
		assertEquals(10.0, destino.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("transferir(): rechaza transferencia con monto cero")
	@Test
	public void testTransferirMontoCero() {
		// Valida que no se permita transferir un monto igual a cero
		
		// Arrange
		Banco banco = new Banco();
		Cuenta origen = new Cuenta("C007");
		Cuenta destino = new Cuenta("C008");
		origen.setSaldoActual(100.0);
		destino.setSaldoActual(30.0);

		// Act
		boolean resultado = banco.transferir(0.0, origen, destino);

		// Assert
		assertFalse(resultado);
		assertEquals(100.0, origen.getSaldoActual(), 0.0001);
		assertEquals(30.0, destino.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("transferir(): rechaza transferencia con monto negativo")
	@Test
	public void testTransferirMontoNegativo() {
		// Valida que no se permita transferir un monto negativo
		
		// Arrange
		Banco banco = new Banco();
		Cuenta origen = new Cuenta("C009");
		Cuenta destino = new Cuenta("C010");
		origen.setSaldoActual(120.0);
		destino.setSaldoActual(40.0);

		// Act
		boolean resultado = banco.transferir(-25.0, origen, destino);

		// Assert
		assertFalse(resultado);
		assertEquals(120.0, origen.getSaldoActual(), 0.0001);
		assertEquals(40.0, destino.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("transferir(): rechaza transferencia hacia la misma cuenta")
	@Test
	public void testTransferirMismaCuenta() {
		// Valida que no se permita transferir dinero hacia la misma cuenta
		
		// Arrange
		Banco banco = new Banco();
		Cuenta cuenta1 = new Cuenta("C011");
		Cuenta cuenta2 = new Cuenta("C011"); // mismo id
		cuenta1.setSaldoActual(90.0);
		cuenta2.setSaldoActual(90.0);

		// Act
		boolean resultado = banco.transferir(20.0, cuenta1, cuenta2);

		// Assert
		assertFalse(resultado);
		assertEquals(90.0, cuenta1.getSaldoActual(), 0.0001);
		assertEquals(90.0, cuenta2.getSaldoActual(), 0.0001);
	}
	
	@DisplayName("transferir(): mantiene saldos intactos cuando la transferencia falla")
	@Test
	public void testNoCambianSaldosCuandoTransferenciaFalla() {
		// Valida que cuando la transferencia no puede realizarse,
		// los saldos de ambas cuentas permanezcan sin cambios
		
		// Arrange
		Banco banco = new Banco();
		Cuenta origen = new Cuenta("C012");
		Cuenta destino = new Cuenta("C013");
		origen.setSaldoActual(30.0);
		destino.setSaldoActual(70.0);

		// Act
		boolean resultado = banco.transferir(50.0, origen, destino);

		// Assert
		assertFalse(resultado);
		assertEquals(30.0, origen.getSaldoActual(), 0.0001);
		assertEquals(70.0, destino.getSaldoActual(), 0.0001);
	}
	
}