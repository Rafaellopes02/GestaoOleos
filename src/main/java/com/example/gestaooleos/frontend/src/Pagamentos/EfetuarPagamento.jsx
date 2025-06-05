import React, { useEffect, useState } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    MenuItem,
    Select,
    FormControl,
    InputLabel
} from '@mui/material';

export default function EfetuarPagamento({ open, onClose, pagamento, metodosPagamento, onPagar }) {
    const [metodoSelecionado, setMetodoSelecionado] = useState('');

    useEffect(() => {
        if (pagamento?.idmetodopagamento) {
            setMetodoSelecionado(pagamento.idmetodopagamento);
        }
    }, [pagamento]);

    const handlePagar = () => {
        if (metodoSelecionado && pagamento) {
            onPagar(pagamento.idpagamento, metodoSelecionado);
        }
    };

    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Efetuar Pagamento</DialogTitle>
            <DialogContent>
                <p><strong>Contrato:</strong> {pagamento?.contrato}</p>
                <p><strong>Valor:</strong> {pagamento?.valor?.toFixed(2)} €</p>

                <FormControl fullWidth sx={{ marginTop: 2 }}>
                    <InputLabel>Método de Pagamento</InputLabel>
                    <Select
                        value={metodoSelecionado}
                        onChange={(e) => setMetodoSelecionado(e.target.value)}
                        label="Método de Pagamento"
                    >
                        {metodosPagamento.map((m) => (
                            <MenuItem key={m.idmetodopagamento} value={m.idmetodopagamento}>
                                {m.metodo}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancelar</Button>
                <Button onClick={handlePagar} variant="contained" color="primary">Pagar</Button>
            </DialogActions>
        </Dialog>
    );
}
