const fs = require('fs');
const path = require('path');
const transactionsFilePath = path.join(__dirname, 'transactions.txt');

// Función para leer el archivo transactions.txt
function leerArchivo() {
    try {
        const data = fs.readFileSync(transactionsFilePath, 'utf8');
        return JSON.parse(data);
    } catch (err) {
        console.error('Error leyendo el archivo:', err);
        return {};
    }
}

// Función para escribir el archivo transactions.txt
function escribirArchivo(data) {
    try {
        fs.writeFileSync(transactionsFilePath, JSON.stringify(data, null, 2), 'utf8');
    } catch (err) {
        console.error('Error escribiendo el archivo:', err);
    }
}

// Función para calcular el saldo actual de un usuario, basado en sus transacciones
function calcularSaldo(usuario) {
    const transacciones = leerArchivo();
    const transaccionesUsuario = transacciones[usuario] || [];
    return transaccionesUsuario.reduce((saldo, transaccion) => saldo + parseFloat(transaccion.balance), 0);
}

// Función para realizar la transferencia entre cuentas
function transferir(de, para, monto) {
    const transacciones = leerArchivo();
    const saldoOrigen = calcularSaldo(de);

    if (saldoOrigen < monto) {
        return {
            exito: false,
            mensaje: `Saldo insuficiente en la cuenta de ${de}.`
        };
    }

    const nuevaTransaccionOrigen = {
        balance: -monto,
        type: 'Withdrawal',
        timestamp: new Date().toISOString()
    };

    const nuevaTransaccionDestino = {
        balance: monto,
        type: 'Deposit',
        timestamp: new Date().toISOString()
    };

    if (!transacciones[de]) {
        transacciones[de] = [];
    }
    if (!transacciones[para]) {
        transacciones[para] = [];
    }

    transacciones[de].push(nuevaTransaccionOrigen);
    transacciones[para].push(nuevaTransaccionDestino);

    escribirArchivo(transacciones);

    return {
        exito: true,
        mensaje: `Transferencia de ${monto} realizada correctamente de ${de} a ${para}.`
    };
}

const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 50);
console.log(resultado.mensaje);

// Exportar las funciones para pruebas
module.exports = { transferir, leerArchivo, escribirArchivo, calcularSaldo };
