// --- Base de Datos Simulada ---
let users = {
    'admin': { id: 'admin', cedula: '111', nombre: 'Admin', apellido: 'Total', login: 'admin', password: '123', rol: 'ADMIN', profileComplete: true },
    'paciente1': { id: 'paciente1', cedula: '222', nombre: 'Carlos', apellido: 'Rojas', login: 'paciente1', password: '123', rol: 'PACIENTE', profileComplete: true },
    'medico1': { id: 'medico1', cedula: '333', nombre: 'Ana', apellido: 'Gómez', login: 'medico1', password: '123', rol: 'MEDICO', profileComplete: true, especialidad: 'Dermatología', costoConsulta: 50000, localidad: 'Heredia', frecuenciaCita: 30, horario: { Lunes: { Manana: ['08:00', '12:00'], Tarde: ['14:00', '18:00'] } } },
    'medico2': { id: 'medico2', cedula: '444', nombre: 'Juan', apellido: 'Pérez', login: 'medico2', password: '123', rol: 'MEDICO', profileComplete: false, especialidad: null, costoConsulta: null, localidad: 'San José', frecuenciaCita: null, horario: {} }
};
let appointments = [
    { id: 1, medicoId: 'medico1', pacienteId: 'paciente1', fecha: '2025-06-10', horaInicio: '09:00', horaFin: '09:30', estado: 'COMPLETADA' },
    { id: 2, medicoId: 'medico1', pacienteId: null, fecha: '2025-06-11', horaInicio: '10:00', horaFin: '10:30', estado: 'ACTIVA' },
    { id: 3, medicoId: 'medico1', pacienteId: 'paciente1', fecha: '2025-06-12', horaInicio: '14:00', horaFin: '14:30', estado: 'PENDIENTE' },
];
let medicosPendientes = [ { id: 'medico-nuevo', nombre: 'Laura', apellido: 'Mora', login: 'medico-nuevo' } ];

export const login = async (login, password) => {
    await new Promise(r => setTimeout(r, 400));
    const user = Object.values(users).find(u => u.login === login && u.password === password);
    if (user) {
        if (user.rol === 'MEDICO' && medicosPendientes.find(m => m.login === user.login)) {
            return { success: false, message: 'Su cuenta aún no ha sido aprobada por un administrador.' };
        }
        return { success: true, user: { id: user.id, nombre: user.nombre, rol: user.rol, profileComplete: user.profileComplete } };
    }
    return { success: false, message: 'Usuario o contraseña incorrectos.' };
};
export const register = async (userData) => {
    await new Promise(r => setTimeout(r, 400));
    if (users[userData.login]) {
        return { success: false, message: 'El login ya existe.' };
    }
    const newUser = { ...userData, id: userData.login };
    users[userData.login] = newUser;
    if (newUser.rol === 'MEDICO') {
        medicosPendientes.push({id: newUser.id, nombre: newUser.nombre, apellido: newUser.apellido, login: newUser.login });
    }
    return { success: true, user: newUser };
};
export const getMedicosPendientes = async () => {
    await new Promise(r => setTimeout(r, 400));
    return [...medicosPendientes];
};
export const aprobarMedico = async (medicoId) => {
    await new Promise(r => setTimeout(r, 400));
    medicosPendientes = medicosPendientes.filter(m => m.id !== medicoId);
    return { success: true };
};
export const getPublicMedicos = async (filters) => {
    await new Promise(r => setTimeout(r, 400));
    const medicosActivos = Object.values(users).filter(u => u.rol === 'MEDICO' && u.profileComplete && !medicosPendientes.some(p => p.id === u.id));
    return medicosActivos.map(m => ({ ...m, activeCitas: [ { date: '2025-06-10', citas: [{id: 10, horaInicio: '09:00'}, {id: 11, horaInicio: '09:30'}] }, { date: '2025-06-11', citas: [{id: 12, horaInicio: '10:00'}] } ] }));
};
export const getCitas = async (userId, userRol) => {
    await new Promise(r => setTimeout(r, 400));
    let userAppointments = (userRol === 'MEDICO')
        ? appointments.filter(c => c.medicoId === userId)
        : appointments.filter(c => c.pacienteId === userId);
    return userAppointments.map(c => ({...c, loginPaciente: users[c.pacienteId]?.login, loginMedico: users[c.medicoId]?.login }));
};
export const getMedicoToComplete = async (medicoId) => {
    await new Promise(r => setTimeout(r, 400));
    return users[medicoId];
};
export const updateMedicoProfile = async (medicoId, data) => {
    await new Promise(r => setTimeout(r, 400));
    users[medicoId] = { ...users[medicoId], ...data, profileComplete: true };
    return { success: true, user: users[medicoId] };
};