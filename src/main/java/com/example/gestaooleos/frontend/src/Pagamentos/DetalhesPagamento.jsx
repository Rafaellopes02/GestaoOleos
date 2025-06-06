// DetalhesPagamento.jsx
import React from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button } from '@mui/material';

export default function DetalhesPagamento({ open, onClose, pagamento }) {
    if (!pagamento) return null;

    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Detalhes do Pagamento</DialogTitle>
            <DialogContent>
                <p><strong>Contrato:</strong> {pagamento.contrato}</p>
                <p><strong>Método:</strong> {pagamento.metodo}</p>
                <p><strong>Valor:</strong> {pagamento.valor.toFixed(2)} €</p>
                <p><strong>Estado:</strong> {pagamento.estado}</p>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Fechar</Button>
            </DialogActions>
        </Dialog>
    );
}
