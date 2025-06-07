import React, { useState } from 'react';
import {
    Modal,
    Box,
    Typography,
    TextField,
    Button
} from '@mui/material';
import { jwtDecode } from 'jwt-decode';

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    backgroundColor: 'white',
    borderRadius: '10px',
    boxShadow: '0 4px 20px rgba(0, 0, 0, 0.2)',
    padding: '30px',
    width: '400px',
};

export default function NovoContrato({ open, onClose, onSave }) {
    const [form, setForm] = useState({
        nome: '',
        dataInicio: '',
        dataFim: '',
        valor: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = () => {
        const token = localStorage.getItem("token");
        const decoded = jwtDecode(token);
        const userId = parseInt(decoded.sub, 10);

        const payload = {
            nome: form.nome,
            dataInicio: form.dataInicio,
            dataFim: form.dataFim,
            idutilizador: userId,
            idEstadoContrato: 4
        };

        if (onSave) onSave(payload);
        onClose();
    };


    return (
        <Modal open={open} onClose={onClose}>
            <Box sx={style}>
                <Typography variant="h6" gutterBottom>
                    Novo Contrato
                </Typography>

                <TextField
                    fullWidth
                    label="Nome"
                    name="nome"
                    value={form.nome}
                    onChange={handleChange}
                    margin="normal"
                />

                <TextField
                    fullWidth
                    label="Data Início"
                    name="dataInicio"
                    type="date"
                    value={form.dataInicio}
                    onChange={handleChange}
                    margin="normal"
                    InputLabelProps={{ shrink: true }}
                />

                <TextField
                    fullWidth
                    label="Data Fim"
                    name="dataFim"
                    type="date"
                    value={form.dataFim}
                    onChange={handleChange}
                    margin="normal"
                    InputLabelProps={{ shrink: true }}
                />

                <Box mt={2} display="flex" justifyContent="flex-end">
                    <Button onClick={onClose} style={{ marginRight: '10px' }}>
                        Cancelar
                    </Button>
                    <Button variant="contained" onClick={handleSubmit} color="primary">
                        Guardar
                    </Button>
                </Box>
            </Box>
        </Modal>
    );
}
