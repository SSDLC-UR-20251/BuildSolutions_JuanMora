const { transferir, leerArchivo, escribirArchivo } = require('../src/app');

beforeEach(() => {
    // Inicializar el archivo de transacciones con datos de prueba
    const transaccionesIniciales = {
        'juan.jose@urosario.edu.co': [
            { balance: 100, type: 'Deposit', timestamp: new Date().toISOString() }
        ],
        'sara.palaciosc@urosario.edu.co': []
    };
    escribirArchivo(transaccionesIniciales);
});

test('Transferencia entre cuentas', () => {
    const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 30);
    expect(resultado.exito).toBe(true);
    expect(resultado.mensaje).toBe('Transferencia de 30 realizada correctamente de juan.jose@urosario.edu.co a sara.palaciosc@urosario.edu.co');

    const transacciones = leerArchivo();
    expect(transacciones['juan.jose@urosario.edu.co'].length).toBe(2);
    expect(transacciones['juan.jose@urosario.edu.co'][1].balance).toBe(-30);
    expect(transacciones['sara.palaciosc@urosario.edu.co'].length).toBe(1);
    expect(transacciones['sara.palaciosc@urosario.edu.co'][0].balance).toBe(30);
});

test('Transferencia con saldo insuficiente', () => {
    const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 1000);
    expect(resultado.exito).toBe(false);
    expect(resultado.mensaje).toBe('Saldo insuficiente en la cuenta de juan.jose@urosario.edu.co');

    const transacciones = leerArchivo();
    expect(transacciones['juan.jose@urosario.edu.co'].length).toBe(1);
    expect(transacciones['sara.palaciosc@urosario.edu.co'].length).toBe(0);
});
