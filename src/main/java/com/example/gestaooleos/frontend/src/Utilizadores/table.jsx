import React, { useEffect, useState } from 'react';

import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { TableVirtuoso } from 'react-virtuoso';

import { FaEye } from "react-icons/fa";

const columns = [
    { width: 200, label: 'Nome', dataKey: 'nome' },
    { width: 150, label: 'Telefone', dataKey: 'telefone' },
    { width: 150, label: 'Tipo Utilizador', dataKey: 'tipo' },
    { width: 100, label: 'Ver', dataKey: 'ver' },
];

const VirtuosoTableComponents = {
    Scroller: React.forwardRef((props, ref) => (
        <TableContainer component={Paper} {...props} ref={ref} />
    )),
    Table: (props) => (
        <Table {...props} sx={{ borderCollapse: 'separate', tableLayout: 'fixed' }} />
    ),
    TableHead: React.forwardRef((props, ref) => (
        <TableHead {...props} ref={ref} />
    )),
    TableRow,
    TableBody: React.forwardRef((props, ref) => (
        <TableBody {...props} ref={ref} />
    )),
};

function fixedHeaderContent() {
    return (
        <TableRow>
            {columns.map((column) => (
                <TableCell
                    key={column.dataKey}
                    variant="head"
                    align="left"
                    style={{ width: column.width }}
                    sx={{ backgroundColor: 'background.paper', fontWeight: 'bold' }}
                >
                    {column.label}
                </TableCell>
            ))}
        </TableRow>
    );
}

function rowContent(_index, row) {
    return (
        <>
            <TableCell>{row.nome}</TableCell>
            <TableCell>{row.telefone}</TableCell>
            <TableCell>{row.tipo}</TableCell>
            <TableCell><FaEye /></TableCell>
        </>
    );
}

export default function TableUtilizadores() {
    const [rows, setRows] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [utilizadoresRes, tiposRes] = await Promise.all([
                    fetch('http://localhost:8080/Utilizadores').then(res => res.json()),
                    fetch('http://localhost:8080/TipoUtilizador').then(res => res.json())
                ]);

                const tipoMap = new Map();
                tiposRes.forEach(tipo => {
                    tipoMap.set(tipo.idtipoutilizador, tipo.nome);
                });

                const utilizadoresComTipo = utilizadoresRes.map(u => ({
                    nome: u.nome,
                    telefone: u.telefone,
                    tipo: tipoMap.get(u.idtipoutilizador) || 'Desconhecido',
                    idutilizador: u.idutilizador
                }));

                setRows(utilizadoresComTipo);
            } catch (error) {
                console.error('Erro ao buscar dados:', error);
            }
        };

        fetchData();
    }, []);


    return (
        <Paper style={{ height: 400, width: '90%', margin: '30px auto' }}>
            <TableVirtuoso
                data={rows}
                components={VirtuosoTableComponents}
                fixedHeaderContent={fixedHeaderContent}
                itemContent={rowContent}
            />
        </Paper>
    );
}
